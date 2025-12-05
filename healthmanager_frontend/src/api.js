import axios from "axios";

const api = axios.create({
  baseURL: "https://healthmanager-backend.onrender.com",
});

api.interceptors.request.use(
  (config) => {

    // 🔥 (수정됨) URL 판별을 더 유연하게: 로그인엔 절대 토큰 안 붙임
    const url = config.url.replace(config.baseURL, "");
    if (url.includes("user/login")) {
      return config;
    }

    const token = localStorage.getItem("token");

    // 🔥 (수정됨) token 문자열만 확실히 넣도록 보장
    if (token && typeof token === "string" && token.trim() !== "") {
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
