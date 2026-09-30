import { Link } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faHouse,
  faBoxOpen,
  faHeart,
  faCartShopping,
  faClipboardList,
  faGaugeHigh,
  faRightToBracket,
  faRightFromBracket,
  faUser,
} from "@fortawesome/free-solid-svg-icons";

import { useAuth } from "../../context/AuthContext";
import { useCart } from "../../context/CartContext";

import "./Navbar.css";

function Navbar() {
  const { authenticated, user, login, logout } = useAuth();
  const { cartCount } = useCart();

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          E-Commerce
        </Link>

        <div className="navbar-links">
          <Link to="/" className="navbar-link">
            <FontAwesomeIcon icon={faHouse} />
            <span>Home</span>
          </Link>

          <Link to="/products" className="navbar-link">
            <FontAwesomeIcon icon={faBoxOpen} />
            <span>Products</span>
          </Link>

          {authenticated && (
            <>
              <Link to="/favorites" className="navbar-link">
                <FontAwesomeIcon icon={faHeart} />
                <span>Favorites</span>
              </Link>

              <Link to="/orders" className="navbar-link">
                <FontAwesomeIcon icon={faClipboardList} />
                <span>Orders</span>
              </Link>

              <Link to="/cart" className="navbar-link navbar-cart">
                <FontAwesomeIcon icon={faCartShopping} />
                <span>Cart</span>

                {cartCount > 0 && (
                  <span className="cart-badge">{cartCount}</span>
                )}
              </Link>
            </>
          )}

          {authenticated && user?.role === "TENANT" && (
            <Link to="/tenant/dashboard" className="navbar-link">
              <FontAwesomeIcon icon={faGaugeHigh} />
              <span>Dashboard</span>
            </Link>
          )}
        </div>

        <div className="navbar-user">
          {authenticated ? (
            <>
              <div className="navbar-user-info">
                <FontAwesomeIcon icon={faUser} />
                <span>{user?.username}</span>
              </div>

              <button
                type="button"
                className="navbar-auth-button"
                onClick={logout}
              >
                <FontAwesomeIcon icon={faRightFromBracket} />
                <span>Logout</span>
              </button>
            </>
          ) : (
            <button
              type="button"
              className="navbar-auth-button"
              onClick={login}
            >
              <FontAwesomeIcon icon={faRightToBracket} />
              <span>Login</span>
            </button>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
