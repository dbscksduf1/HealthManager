import axios from "axios";

const api = axios.create({
  baseURL: "https://healthmanager-backend.onrender.com/",
  withCredentials: true,
});

api.interceptors.request.use(
  (config) => {

    // 🔥 로그인 요청 절대 토큰 붙이지 않도록 URL 정규화
    const cleanUrl = config.url.replace(config.baseURL, "");
    if (cleanUrl === "/user/login") {
      return config;
    }

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
