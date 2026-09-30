import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHeart } from "@fortawesome/free-solid-svg-icons";

import "./FavoriteButton.css";

function FavoriteButton({ isFavorite, onClick, disabled = false }) {
  return (
    <button
      type="button"
      className={`favorite-button ${isFavorite ? "favorite-active" : ""}`}
      onClick={onClick}
      disabled={disabled}
      aria-label={isFavorite ? "Remove from favorites" : "Add to favorites"}
      //for readers
      title={isFavorite ? "Remove from favorites" : "Add to favorites"}
    >
      <FontAwesomeIcon icon={faHeart} />
    </button>
  );
}

export default FavoriteButton;
