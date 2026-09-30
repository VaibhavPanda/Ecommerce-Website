import api from "./api";

export const createOrder = (items) => {
  return api.post("/orders", {
    items,
  });
};

export const getOrders = () => {
  return api.get("/orders");
};

export const getOrder = (orderId) => {
  return api.get(`/orders/${orderId}`);
};
