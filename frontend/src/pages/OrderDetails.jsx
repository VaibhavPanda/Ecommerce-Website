import { useEffect, useState } from "react";

import { Link, useParams } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowLeft,
  faBoxOpen,
  faCartShopping,
  faCheck,
} from "@fortawesome/free-solid-svg-icons";

import { getOrder } from "../services/orderService";

import "./OrderDetails.css";

function OrderDetails() {
  const { orderId } = useParams();

  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadOrder = async () => {

      ///order/abc -> prevents
      const numericOrderId = Number(orderId);

      if (!Number.isInteger(numericOrderId) || numericOrderId <= 0) {
      setError("Invalid order ID.");
      setLoading(false);
      return;
      }

      try {
        setLoading(true);
        setError("");

        const response = await getOrder(orderId);

        setOrder(response.data);
      } catch (error) {
        console.error("Failed to load order:", error);

        setError(error.response?.data?.message || "Order not found.");
      } finally {
        setLoading(false);
      }
    };

    loadOrder();
  }, [orderId]);

  if (loading) {
    return (
      <main className="order-details-state">
        <div className="order-details-loader" />
        <p>Loading order...</p>
      </main>
    );
  }

  if (error) {
    return (
      <main className="order-details-state">
        <div className="order-details-state-icon">!</div>

        <h2>{error}</h2>

        <Link to="/orders" className="order-details-primary-button">
          <FontAwesomeIcon icon={faArrowLeft} />
          Back to Orders
        </Link>
      </main>
    );
  }

  if (!order) {
    return null;
  }

  return (
    <main className="order-details-page">
      <Link to="/orders" className="order-details-back-link">
        <FontAwesomeIcon icon={faArrowLeft} />
        Back to Orders
      </Link>

      <header className="order-details-header">
        <div>
          <p className="order-details-eyebrow">ORDER DETAILS</p>

          <h1>Order #{order.orderId}</h1>

          <p>Placed on {new Date(order.orderDate).toLocaleString("en-IN")}</p>
        </div>

        <div className="order-status">
          <FontAwesomeIcon icon={faCheck} />
          Order Placed
        </div>
      </header>

      <div className="order-details-layout">
        <section className="order-items-section">
          <div className="order-section-header">
            <h2>Items</h2>

            <span>
              {order.totalQuantity}{" "}
              {order.totalQuantity === 1 ? "item" : "items"}
            </span>
          </div>

          <div className="order-items-list">
            {order.items.map((item) => (
              <article className="order-item" key={item.productId}>
                <div className="order-item-main">
                  <div className="order-item-icon">
                    <FontAwesomeIcon icon={faBoxOpen} />
                  </div>

                  <div>
                    <h3>{item.productName}</h3>

                    <p>
                      ₹{Number(item.price).toLocaleString("en-IN")} ×{" "}
                      {item.quantity}
                    </p>
                  </div>
                </div>

                <strong>
                  ₹{Number(item.subtotal).toLocaleString("en-IN")}
                </strong>
              </article>
            ))}
          </div>
        </section>

        <aside className="order-summary">
          <h2>Order Summary</h2>

          <div className="order-summary-row">
            <span>Total Quantity</span>
            <strong>{order.totalQuantity}</strong>
          </div>

          <div className="order-summary-divider" />

          <div className="order-summary-total">
            <span>Total Amount</span>

            <strong>
              ₹{Number(order.totalAmount).toLocaleString("en-IN")}
            </strong>
          </div>

          <Link to="/products" className="order-shop-button">
            <FontAwesomeIcon icon={faCartShopping} />
            Continue Shopping
          </Link>
        </aside>
      </div>
    </main>
  );
}

export default OrderDetails;
