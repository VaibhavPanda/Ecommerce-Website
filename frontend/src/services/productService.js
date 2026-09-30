import api from "./api";

export const getProducts = (params = {}) => {
  return api.get("/products", {
    params,
  });
};

export const getProduct = (productId) => {
  return api.get(`/products/${productId}`);
};
