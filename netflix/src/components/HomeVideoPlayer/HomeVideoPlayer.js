import React, { useRef, useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import MainMovie from "../mainMovie/mainMovie";
import Popup from "../popup/Popup"; 
import './HomeVideoPlayer.css';

const HomeVideoPlayer = ({ Movie }) => {
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [isMuted, setIsMuted] = useState(true); 
  const videoRef = useRef(null);
  const navigate = useNavigate();
  const API_URL = process.env.REACT_APP_API_URL;  
  // open popup
  const handleDetailsClick = () => {
    setIsPopupOpen(true); 
  };
  // play video
  const handlePlay = () => {
    navigate("/videoPlayer", { state: { videoSrc: Movie.trailer, movieId: Movie._id } });
  };
  // close popup
  const handleClose = () => {
    setIsPopupOpen(false); 
  };
  // mute/unmute video
  useEffect(() => {
    const video = videoRef.current;

    if (video) {
      video.muted = true; 
      video.play().catch((err) => {
        console.warn("Autoplay failed. Video is muted:", err);
      });
    }
  }, []);

  const toggleMute = () => {
    const video = videoRef.current;

    if (video) {
      video.muted = !isMuted; 
      setIsMuted(!isMuted);
    }
  };
  // display video player
  return (
    <>
    <div className="video-player">
  <video
    ref={videoRef}
    src={`${API_URL}/${Movie.trailer}`}
    className="video"
    preload="metadata"
    autoPlay
    loop
    muted
  >
    <source src={`${API_URL}/${Movie.trailer}`} type="video/mp4" />
  </video>

  <div className="name">{Movie.name}</div>

  <button className="mute-button" onClick={toggleMute}>
      {isMuted ? (
        <i className="bi bi-volume-mute-fill"></i> 
      ) : (
        <i className="bi bi-volume-up-fill"></i> 
      )}
    </button>

  <div className="right-controls">
    <button type="button" className="btn btn-secondary" onClick={handleDetailsClick}>Info</button>
    <button type="button" className="btn btn-light" onClick={handlePlay}>Play</button>
  </div>
</div>
    {/*Popup */}
      <Popup isOpen={isPopupOpen} onClose={handleClose}>
        <MainMovie movie={Movie} similarMovies={null} />
      </Popup>
    </>
    
  );
};

export default HomeVideoPlayer;
