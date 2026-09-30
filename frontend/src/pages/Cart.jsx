import { Link } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowLeft,
  faArrowRight,
  faCartShopping,
  faMinus,
  faPlus,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";

import { useCart } from "../context/CartContext";

import "./Cart.css";

function Cart() {
  const { cartItems, removeFromCart, updateQuantity, cartTotal } = useCart();

  if (cartItems.length === 0) {
    return (
      <main className="cart-page">
        <div className="cart-empty">
          <div className="cart-empty-icon">
            <FontAwesomeIcon icon={faCartShopping} />
          </div>

          <h1>Your Cart is Empty</h1>

          <p>You haven't added any products to your cart yet.</p>

          <Link to="/products" className="cart-primary-button">
            <FontAwesomeIcon icon={faArrowLeft} />
            Continue Shopping
          </Link>
        </div>
      </main>
    );
  }

  return (
    <main className="cart-page">
      <header className="cart-header">
        <div>
          <p className="cart-eyebrow">SHOPPING CART</p>
          <h1>Your Cart</h1>
          <p className="cart-item-count">
            {cartItems.length} {cartItems.length === 1 ? "product" : "products"}{" "}
            in your cart
          </p>
        </div>
      </header>

      <div className="cart-layout">
        <section className="cart-items">
          {cartItems.map((item) => {
            const itemSubtotal = Number(item.price) * item.quantity;

            return (
              <article className="cart-item" key={item.productId}>
                <div className="cart-item-details">
                  <div className="cart-item-info">
                    <h2>{item.name}</h2>

                    <p className="cart-item-price">
                      ₹{Number(item.price).toLocaleString("en-IN")}
                    </p>
                  </div>

                  <button
                    type="button"
                    className="cart-remove-button"
                    onClick={() => removeFromCart(item.productId)}
                    aria-label={`Remove ${item.name} from cart`}
                  >
                    <FontAwesomeIcon icon={faTrash} />
                    <span>Remove</span>
                  </button>
                </div>

                <div className="cart-item-bottom">
                  <div className="cart-quantity">
                    <button
                      type="button"
                      onClick={() =>
                        updateQuantity(item.productId, item.quantity - 1)
                      }
                      disabled={item.quantity <= 1}
                      aria-label="Decrease quantity"
                    >
                      <FontAwesomeIcon icon={faMinus} />
                    </button>

                    <span>{item.quantity}</span>

                    <button
                      type="button"
                      onClick={() =>
                        updateQuantity(item.productId, item.quantity + 1)
                      }
                      disabled={item.quantity >= item.availableQuantity}
                      aria-label="Increase quantity"
                    >
                      <FontAwesomeIcon icon={faPlus} />
                    </button>
                  </div>

                  <div className="cart-item-subtotal">
                    <span>Subtotal</span>
                    <strong>₹{itemSubtotal.toLocaleString("en-IN")}</strong>
                  </div>
                </div>
              </article>
            );
          })}
        </section>

        <aside className="cart-summary">
          <h2>Order Summary</h2>

          <div className="cart-summary-row">
            <span>Products</span>
            <span>{cartItems.length}</span>
          </div>

          <div className="cart-summary-divider" />

          <div className="cart-total">
            <span>Total</span>
            <strong>₹{cartTotal.toLocaleString("en-IN")}</strong>
          </div>

          <Link to="/checkout" className="cart-checkout-button">
            <span>Proceed to Checkout</span>
            <FontAwesomeIcon icon={faArrowRight} />
          </Link>

          <Link to="/products" className="cart-continue-link">
            <FontAwesomeIcon icon={faArrowLeft} />
            Continue Shopping
          </Link>
        </aside>
      </div>
    </main>
  );
}

export default Cart;
