import api from "./api";

export const getCategories = () => {
  return api.get("/categories");
};

export const getTenantCategories = (tenantDomain) => {
  return api.get(`/${tenantDomain}/categories`);
};

export const createTenantCategory = (tenantDomain, category) => {
  return api.post(`/${tenantDomain}/categories`, category);
};

export const updateTenantCategory = (tenantDomain, categoryId, category) => {
  return api.put(`/${tenantDomain}/categories/${categoryId}`, category);
};

export const deleteTenantCategory = (tenantDomain, categoryId) => {
  return api.delete(`/${tenantDomain}/categories/${categoryId}`);
};
