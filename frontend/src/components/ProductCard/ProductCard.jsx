import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowRight, faTag } from "@fortawesome/free-solid-svg-icons";

import "./ProductCard.css";

import FavoriteButton from "../FavoriteButton/FavoriteButton";

function ProductCard({
  product,
  isFavorite = false,
  onFavoriteToggle,
  favoriteLoading = false,
}) {
  return (
    <article className="product-card">
      <div className="product-card-content">
        <div className="product-card-top">
          <span className="product-card-category">
            <FontAwesomeIcon icon={faTag} />
            {product.categoryName}
          </span>

          {onFavoriteToggle && (
            <FavoriteButton
              isFavorite={isFavorite}
              onClick={() => onFavoriteToggle(product.id)}
              disabled={favoriteLoading}
            />
          )}
        </div>

        <div className="product-card-main">
          <h2 className="product-card-name">{product.name}</h2>

          <p className="product-card-description">
            {product.description || "No description available."}
          </p>
        </div>

        <div className="product-card-info">
          <div>
            <span className="product-card-price">
              ₹{Number(product.price).toLocaleString("en-IN")}
            </span>
          </div>

          <span className="product-card-tenant">{product.tenantName}</span>
        </div>

        <div className="product-card-actions">
          <Link to={`/products/${product.id}`} className="product-card-button">
            <span>View Product</span>
            <FontAwesomeIcon icon={faArrowRight} />
          </Link>
        </div>
      </div>
    </article>
  );
}

export default ProductCard;
