import React, { useRef, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLocation } from "react-router-dom";
import './VideoPlayer.css';

const VideoPlayer = () => {
    // return video player
  const [isPlaying, setIsPlaying] = useState(false);
  const [volume, setVolume] = useState(1);
  const [isFullScreen, setIsFullScreen] = useState(false);
  const [isControlsVisible, setIsControlsVisible] = useState(false);
  const [buffered, setBuffered] = useState(0);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [errorMessage, setErrorMessage] = useState("");
  const videoRef = useRef(null);
  const timeoutRef = useRef(null);
  const progressRef = useRef(null);
  const location = useLocation();
  const videoSrc = location.state?.videoSrc;
  const movieId = location.state?.movieId; 
  const token = localStorage.getItem("jwtToken");
  const navigate = useNavigate();
  const API_URL = process.env.REACT_APP_API_URL;
  // format time
  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
  };
  // handle mouse move
  const handleMouseMove = () => {
    setIsControlsVisible(true);
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }
    // hide controls after 3 seconds
    timeoutRef.current = setTimeout(() => {
      setIsControlsVisible(false);
    }, 3000);
  };
   // handle mouse leave
  const handleMouseLeave = () => {
    setIsControlsVisible(false);
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }
  };
   // useEffect to handle time update, loaded metadata and progress
  useEffect(() => {
    const video = videoRef.current;
   // handle time update
    const handleTimeUpdate = () => {
      setCurrentTime(video.currentTime);
    };
    // handle loaded metadata
    const handleLoadedMetadata = () => {
      setDuration(video.duration);
    };
    // handle progress
    const handleProgress = () => {
      if (video.buffered.length > 0) {
        const bufferedEnd = video.buffered.end(video.buffered.length - 1);
        const duration = video.duration;
        setBuffered((bufferedEnd / duration) * 100);
      }
    };
    // add event listeners
    video.addEventListener('timeupdate', handleTimeUpdate);
    video.addEventListener('loadedmetadata', handleLoadedMetadata);
    video.addEventListener('progress', handleProgress);
    // remove event listeners
    return () => {
      video.removeEventListener('timeupdate', handleTimeUpdate);
      video.removeEventListener('loadedmetadata', handleLoadedMetadata);
      video.removeEventListener('progress', handleProgress);
    };
  }, []);
    // useEffect to update that the user has watched the movie
  useEffect(() => {
    const sendRecommendationRequest = async () => {
      try {
        console.log("movieId", movieId);
        const response = await fetch(`${API_URL}/api/movies/${movieId}/recommend`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,

          },
        });

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.error || "Failed to send recommendation request.");
        }
      } catch (error) {
        setErrorMessage(error.message); 
      }
    };

    if (movieId) {
      sendRecommendationRequest();
    }
  }, [movieId, token]);
  // handle progress click 
  const handleProgressClick = (e) => {
    const progressBar = progressRef.current;
    const rect = progressBar.getBoundingClientRect();
    const pos = (e.clientX - rect.left) / progressBar.offsetWidth;
    const newTime = pos * videoRef.current.duration;
    videoRef.current.currentTime = newTime;
    setCurrentTime(newTime);
  };
  // toggle play pause
  const togglePlayPause = () => {
    if (errorMessage) {
      alert("Cannot play the video due to an error: " + errorMessage);
      return; 
    }
  
    if (isPlaying) {
      videoRef.current.pause();
    } else {
      videoRef.current.play();
    }
    setIsPlaying(!isPlaying);
  };
   // handle volume change
  const handleVolumeChange = (e) => {
    const newVolume = e.target.value;
    setVolume(newVolume);
    videoRef.current.volume = newVolume;
  };
   // toggle full screen
  const toggleFullScreen = () => {
    if (!document.fullscreenElement) {
      videoRef.current.requestFullscreen();
      setIsFullScreen(true);
    } else {
      document.exitFullscreen();
      setIsFullScreen(false);
    }
  };

  const handleClose = () => {
    navigate(-1); 
  };
  // return video player with controls
  return (
    <div
      className="video-player1"
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
    >
      <button className="close-button" onClick={handleClose}>
        ✕
      </button>
      {errorMessage ? (
        <div className="error-message">
          <p>{errorMessage}</p>
          <p>Unable to play the video. Please try again later.</p>
        </div>
      ) : (
        <video
          ref={videoRef}
          src={`${API_URL}/${videoSrc}`}
          className="video1"
          onClick={togglePlayPause}
          preload="metadata"
        >
          <source src={videoSrc} type="video/mp4" />
        </video>
      )}
  
      {!errorMessage && (
        <div className={`controls ${isControlsVisible ? 'visible' : ''}`}>
          <div
            ref={progressRef}
            className="progress-container"
            onClick={handleProgressClick}
          >
            <div className="progress-bar">
              <div
                className="buffered-progress"
                style={{ width: `${buffered}%` }}
              />
              <div
                className="time-progress"
                style={{ width: `${(currentTime / duration) * 100}%` }}
              />
            </div>
            <div className="time-display">
              <span>{formatTime(currentTime)}</span>
              <span>{formatTime(duration)}</span>
            </div>
          </div>
          <div className="left-controls">
            <button className="control-button" onClick={togglePlayPause}>
              {isPlaying ? '⏸' : '▶'}
            </button>
            <div className="volume-control">
              <input
                type="range"
                min="0"
                max="1"
                step="0.1"
                value={volume}
                onChange={handleVolumeChange}
                className="volume-slider"
              />
            </div>
          </div>
          <div className="right-controls">
            <button className="control-button" onClick={toggleFullScreen}>
              ⛶
            </button>
          </div>
        </div>
      )}
    </div>
  );
  
};

export default VideoPlayer;
