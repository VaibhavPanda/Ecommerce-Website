import { createContext, useContext, useEffect, useState } from "react";

const CartContext = createContext(null);

function getInitialCart() {
  try {
    const storedCart = localStorage.getItem("cart");

    return storedCart ? JSON.parse(storedCart) : [];
  } catch (error) {
    console.error("Failed to load cart:", error);
    return [];
  }
}

export function CartProvider({ children }) {
  const [cartItems, setCartItems] = useState(getInitialCart);

  useEffect(() => {
    localStorage.setItem("cart", JSON.stringify(cartItems));
  }, [cartItems]);

  const addToCart = (product, quantity = 1) => {
    setCartItems((currentItems) => {
      const existingItem = currentItems.find(
        (item) => item.productId === product.id,
      );

      if (existingItem) {
        const newQuantity = Math.min(
          existingItem.quantity + quantity,
          product.quantity,
        );

        return currentItems.map((item) =>
          item.productId === product.id
            ? {
                ...item,
                name: product.name,
                price: product.price,
                availableQuantity: product.quantity,
                quantity: newQuantity,
              }
            : item,
        );
      }

      const safeQuantity = Math.min(quantity, product.quantity);

      return [
        ...currentItems,
        {
          productId: product.id,
          name: product.name,
          price: product.price,
          availableQuantity: product.quantity,
          quantity: safeQuantity,
        },
      ];
    });
  };

  const removeFromCart = (productId) => {
    setCartItems((currentItems) =>
      currentItems.filter((item) => item.productId !== productId),
    );
  };

  const updateQuantity = (productId, quantity) => {
    setCartItems((currentItems) =>
      currentItems
        .map((item) => {
          if (item.productId !== productId) {
            return item;
          }

          const safeQuantity = Math.min(quantity, item.availableQuantity);

          return {
            ...item,
            quantity: safeQuantity,
          };
        })
        .filter((item) => item.quantity > 0),
    );
  };

  const clearCart = () => {
    setCartItems([]);
  };

  const cartCount = cartItems.reduce((total, item) => total + item.quantity, 0);

  const cartTotal = cartItems.reduce(
    (total, item) => total + Number(item.price) * item.quantity,
    0,
  );

  return (
    <CartContext.Provider
      value={{
        cartItems,
        addToCart,
        removeFromCart,
        updateQuantity,
        clearCart,
        cartCount,
        cartTotal,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  return useContext(CartContext);
}
