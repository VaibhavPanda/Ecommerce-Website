import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowLeft,
  faBoxOpen,
  faCheck,
} from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../../context/AuthContext";
import {
  getTenantProduct,
  updateTenantProduct,
} from "../../services/tenantProductService";
import { getTenantCategories } from "../../services/categoryService";
import "./TenantProductForm.css";

function EditProduct() {
  const { user } = useAuth();
  const { productId } = useParams();
  const navigate = useNavigate();
  const tenantDomain = user?.tenant?.domain;

  const [categories, setCategories] = useState([]);

  const [formData, setFormData] = useState({
    name: "",
    description: "",
    price: "",
    quantity: "",
    categoryId: "",
  });

  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!tenantDomain) {
      setLoading(false);
      return;
    }

    const loadData = async () => {
      try {
        setLoading(true);
        setError("");

        const [productResponse, categoriesResponse] = await Promise.all([
          getTenantProduct(tenantDomain, productId),
          getTenantCategories(tenantDomain),
        ]);

        const product = productResponse.data;

        setFormData({
          name: product.name || "",
          description: product.description || "",
          price: product.price ?? "",
          quantity: product.quantity ?? "",
          categoryId: product.categoryId ?? "",
        });

        setCategories(categoriesResponse.data);
      } catch (error) {
        console.error("Failed to load product:", error);

        setError(error.response?.data?.message || "Failed to load product.");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [tenantDomain, productId]);

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setSubmitting(true);
    setError("");

    try {
      const product = {
        name: formData.name.trim(),
        description: formData.description.trim(),
        price: Number(formData.price),
        quantity: Number(formData.quantity),
        categoryId: Number(formData.categoryId),
      };

      await updateTenantProduct(tenantDomain, productId, product);

      navigate("/tenant/products");
    } catch (error) {
      console.error("Failed to update product:", error);

      setError(error.response?.data?.message || "Failed to update product.");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <main className="tenant-product-form-state">
        <div className="tenant-product-form-loader" />
        <p>Loading product...</p>
      </main>
    );
  }

  if (error && !formData.name) {
    return (
      <main className="tenant-product-form-state">
        <div className="tenant-product-form-state-icon">!</div>

        <h2>Unable to load product</h2>

        <p>{error}</p>

        <Link to="/tenant/products" className="tenant-product-submit-button">
          <FontAwesomeIcon icon={faArrowLeft} />
          Back to Products
        </Link>
      </main>
    );
  }

  return (
    <main className="tenant-product-form-page">
      <Link to="/tenant/products" className="tenant-product-form-back-link">
        <FontAwesomeIcon icon={faArrowLeft} />
        Back to Products
      </Link>

      <header className="tenant-product-form-header">
        <div className="tenant-product-form-icon">
          <FontAwesomeIcon icon={faBoxOpen} />
        </div>

        <div>
          <p className="tenant-product-form-eyebrow">PRODUCT MANAGEMENT</p>

          <h1>Edit Product</h1>

          <p>
            Update the details of <strong>{formData.name}</strong>.
          </p>
        </div>
      </header>

      {error && (
        <div className="tenant-product-form-error">
          <strong>Unable to update product</strong>
          <span>{error}</span>
        </div>
      )}

      <form className="tenant-product-form" onSubmit={handleSubmit}>
        <div className="tenant-product-form-section">
          <div className="tenant-product-form-section-heading">
            <h2>Product Information</h2>
            <span>Update product details</span>
          </div>

          <div className="tenant-product-form-grid">
            <div className="tenant-product-form-field full-width">
              <label htmlFor="name">Product Name</label>

              <input
                id="name"
                name="name"
                type="text"
                value={formData.name}
                onChange={handleChange}
                placeholder="Enter product name"
                maxLength={255}
                required
              />
            </div>

            <div className="tenant-product-form-field full-width">
              <label htmlFor="description">Description</label>

              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Describe the product"
                rows={5}
                maxLength={2000}
              />
            </div>

            <div className="tenant-product-form-field">
              <label htmlFor="price">Price</label>

              <div className="tenant-product-input-prefix">
                <span>₹</span>

                <input
                  id="price"
                  name="price"
                  type="number"
                  min="0.01"
                  step="0.01"
                  value={formData.price}
                  onChange={handleChange}
                  placeholder="0.00"
                  required
                />
              </div>
            </div>

            <div className="tenant-product-form-field">
              <label htmlFor="quantity">Quantity</label>

              <input
                id="quantity"
                name="quantity"
                type="number"
                min="0"
                step="1"
                value={formData.quantity}
                onChange={handleChange}
                placeholder="0"
                required
              />
            </div>

            <div className="tenant-product-form-field full-width">
              <label htmlFor="categoryId">Category</label>

              <select
                id="categoryId"
                name="categoryId"
                value={formData.categoryId}
                onChange={handleChange}
                required
              >
                <option value="">Select a category</option>

                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>

        <div className="tenant-product-form-actions">
          <Link to="/tenant/products" className="tenant-product-cancel-button">
            Cancel
          </Link>

          <button
            type="submit"
            className="tenant-product-submit-button"
            disabled={submitting}
          >
            <FontAwesomeIcon icon={faCheck} />

            {submitting ? "Updating..." : "Update Product"}
          </button>
        </div>
      </form>
    </main>
  );
}

export default EditProduct;
