import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowRight,
  faCartShopping,
  faHeart,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";

import { getFavorites, removeFavorite } from "../services/favoriteService";

import "./Favorites.css";

function Favorites() {
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [removingId, setRemovingId] = useState(null);

  useEffect(() => {
    const loadFavorites = async () => {
      try {
        setLoading(true);
        setError("");

        const response = await getFavorites();

        setFavorites(response.data);
      } catch (error) {
        console.error("Failed to load favorites:", error);

        setError(error.response?.data?.message || "Failed to load favorites.");
      } finally {
        setLoading(false);
      }
    };

    loadFavorites();
  }, []);

  const handleRemove = async (productId) => {
    try {
      setRemovingId(productId);

      await removeFavorite(productId);

      setFavorites((current) =>
        current.filter((favorite) => favorite.productId !== productId),
      );
    } catch (error) {
      console.error("Failed to remove favorite:", error);
    } finally {
      setRemovingId(null);
    }
  };

  if (loading) {
    return (
      <main className="favorites-state">
        <div className="favorites-loader" />
        <p>Loading favorites...</p>
      </main>
    );
  }

  if (error) {
    return (
      <main className="favorites-state">
        <div className="favorites-state-icon">!</div>

        <h2>Unable to load favorites</h2>

        <p>{error}</p>

        <Link to="/products" className="favorites-primary-button">
          <FontAwesomeIcon icon={faCartShopping} />
          Browse Products
        </Link>
      </main>
    );
  }

  if (favorites.length === 0) {
    return (
      <main className="favorites-page">
        <div className="favorites-empty">
          <div className="favorites-empty-icon">
            <FontAwesomeIcon icon={faHeart} />
          </div>

          <h1>No Favorites Yet</h1>

          <p>Save products you love and they will appear here.</p>

          <Link to="/products" className="favorites-primary-button">
            <FontAwesomeIcon icon={faCartShopping} />
            Browse Products
          </Link>
        </div>
      </main>
    );
  }

  return (
    <main className="favorites-page">
      <header className="favorites-header">
        <p className="favorites-eyebrow">YOUR COLLECTION</p>

        <h1>My Favorites</h1>

        <p>Products you've saved for later.</p>
      </header>

      <section className="favorites-list">
        {favorites.map((favorite) => (
          <article className="favorite-item" key={favorite.id}>
            <div className="favorite-item-main">
              <div className="favorite-icon">
                <FontAwesomeIcon icon={faHeart} />
              </div>

              <div className="favorite-item-info">
                <h2>{favorite.productName}</h2>

                <p>₹{Number(favorite.price).toLocaleString("en-IN")}</p>
              </div>
            </div>

            <div className="favorite-actions">
              <Link
                to={`/products/${favorite.productId}`}
                className="favorite-view-button"
              >
                <span>View Product</span>

                <FontAwesomeIcon icon={faArrowRight} />
              </Link>

              <button
                type="button"
                className="favorite-remove-button"
                onClick={() => handleRemove(favorite.productId)}
                disabled={removingId === favorite.productId}
                aria-label={`Remove ${favorite.productName} from favorites`}
              >
                <FontAwesomeIcon icon={faTrash} />
                <span>
                  {removingId === favorite.productId ? "Removing..." : "Remove"}
                </span>
              </button>
            </div>
          </article>
        ))}
      </section>
    </main>
  );
}

export default Favorites;
