import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import "./WelcomePage.css";

const WelcomePage = () => {
    const [signUpClick, setsignUpClicked] = useState(false);
    const [signInClick, setsignInClicked] = useState(false);

    const navigate = useNavigate();

    const handleClick = () => {
        setsignUpClicked(true);
        navigate("/signup");
    }
    const handleClickSignIn = () => {
        setsignInClicked(true);
        navigate("/login");
    }
    // display WelcomePage component
  return (
    <div className="hero-section">
      <header className="header">
        <img
          src="/projflix_logo.svg"
          alt="Projflix Logo"
          className="logo"
        />
        <button className="sign-in-button" onClick={handleClickSignIn}>Sign In</button>
      </header>

      <div className="overlay"></div>
      <div className="content">
        <h1>Unlimited movies, TV shows, and more.</h1>
        <p>Watch anywhere. Cancel anytime.</p>
        <p>
          Ready to watch? Enter your email to create or restart your membership.
        </p>
        <div className="form-container">
          <button className="cta-button" onClick={handleClick}>Get Started</button>
        </div>
      </div>
    </div>
  );
};

export default WelcomePage;
