import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowLeft,
  faMinus,
  faPlus,
  faCartShopping,
  faBoxOpen,
} from "@fortawesome/free-solid-svg-icons";

import { getProduct } from "../services/productService";
import { useCart } from "../context/CartContext";

import "./ProductDetails.css";

function ProductDetails() {
  const { productId } = useParams();

  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const { addToCart } = useCart();

  useEffect(() => {
    const loadProduct = async () => {
      try {
        setLoading(true);
        setError("");
        setQuantity(1);

        const response = await getProduct(productId);

        setProduct(response.data);
      } catch (error) {
        console.error("Failed to load product:", error);
        setError("Unable to load product.");
      } finally {
        setLoading(false);
      }
    };

    loadProduct();
  }, [productId]);

  const decreaseQuantity = () => {
    setQuantity((currentQuantity) => Math.max(1, currentQuantity - 1));
  };

  const increaseQuantity = () => {
    setQuantity((currentQuantity) =>
      Math.min(product.quantity, currentQuantity + 1),
    );
  };

  const handleAddToCart = () => {
    addToCart(product, quantity);
  };

  if (loading) {
    return (
      <main className="product-details-state">
        <div className="product-details-loader" />
        <p>Loading product...</p>
      </main>
    );
  }

  if (error) {
    return (
      <main className="product-details-state">
        <div className="product-details-state-icon">!</div>

        <h2>{error}</h2>

        <Link to="/products" className="product-details-back-button">
          <FontAwesomeIcon icon={faArrowLeft} />
          Back to Products
        </Link>
      </main>
    );
  }

  if (!product) {
    return (
      <main className="product-details-state">
        <div className="product-details-state-icon">
          <FontAwesomeIcon icon={faBoxOpen} />
        </div>

        <h2>Product not found.</h2>

        <Link to="/products" className="product-details-back-button">
          <FontAwesomeIcon icon={faArrowLeft} />
          Back to Products
        </Link>
      </main>
    );
  }

  const isOutOfStock = product.quantity <= 0;

  return (
    <main className="product-details">
      <Link to="/products" className="back-link">
        <FontAwesomeIcon icon={faArrowLeft} />
        Back to Products
      </Link>

      <div className="product-details-card">
        <div className="product-details-info">
          <span className="product-details-category">
            {product.categoryName}
          </span>

          <h1>{product.name}</h1>

          <p className="product-details-description">
            {product.description || "No description available."}
          </p>

          <div className="product-details-price">
            ₹{Number(product.price).toLocaleString("en-IN")}
          </div>

          <div className="product-details-meta">
            <div className="product-details-meta-item">
              <span>Brand</span>
              <strong>{product.tenantName}</strong>
            </div>

            <div className="product-details-meta-item">
              <span>Availability</span>

              <strong
                className={isOutOfStock ? "stock-out" : "stock-available"}
              >
                {isOutOfStock ? "Out of stock" : "In stock"}
              </strong>
            </div>
          </div>

          {!isOutOfStock && (
            <>
              <div className="product-stock-info">
                <FontAwesomeIcon icon={faBoxOpen} />

                <span>
                  {product.quantity} {product.quantity === 1 ? "item" : "items"}{" "}
                  available
                </span>
              </div>

              <div className="product-purchase">
                <div className="product-quantity">
                  <span className="quantity-label">Quantity</span>

                  <div className="quantity-controls">
                    <button
                      type="button"
                      onClick={decreaseQuantity}
                      disabled={quantity <= 1}
                      aria-label="Decrease quantity"
                    >
                      <FontAwesomeIcon icon={faMinus} />
                    </button>

                    <span>{quantity}</span>

                    <button
                      type="button"
                      onClick={increaseQuantity}
                      disabled={quantity >= product.quantity}
                      aria-label="Increase quantity"
                    >
                      <FontAwesomeIcon icon={faPlus} />
                    </button>
                  </div>
                </div>

                <button
                  type="button"
                  className="add-to-cart-button"
                  onClick={handleAddToCart}
                >
                  <FontAwesomeIcon icon={faCartShopping} />
                  Add to Cart
                </button>
              </div>
            </>
          )}

          {isOutOfStock && (
            <button
              type="button"
              className="add-to-cart-button add-to-cart-disabled"
              disabled
            >
              Out of Stock
            </button>
          )}
        </div>
      </div>
    </main>
  );
}

export default ProductDetails;
