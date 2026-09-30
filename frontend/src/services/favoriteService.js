import api from "./api";

export const getFavorites = () => {
  return api.get("/favorites");
};

export const addFavorite = (productId) => {
  return api.post(`/favorites/${productId}`);
};

export const removeFavorite = (productId) => {
  return api.delete(`/favorites/${productId}`);
};


// GET    /api/favorites
// POST   /api/favorites/{productId}
// DELETE /api/favorites/{productId}
