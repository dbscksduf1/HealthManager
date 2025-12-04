import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8081/",
});

api.interceptors.request.use(
  (config) => {
    // 🔥 로그인 요청에는 토큰 절대 붙이지 않음
    if (config.url.includes("/user/login")) {
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
