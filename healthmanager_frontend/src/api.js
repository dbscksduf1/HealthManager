import axios from "axios";

const api = axios.create({
  baseURL: "https://healthmanager-backend.onrender.com",
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");

    if (token && token !== "null" && token !== "undefined" && token.trim() !== "") {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const msg = error.response?.data?.error;

    if (msg) {
      alert(msg);
    }

    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/";
    }

    return Promise.reject(error);
  }
);

export default api;
