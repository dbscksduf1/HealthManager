import axios from "axios";

const api = axios.create({
  baseURL: "https://healthmanager-dxh7.onrender.com",
});

// ========================================
// 🔥 요청 인터셉터 (Authorization 처리)
// ========================================
api.interceptors.request.use(
  (config) => {

    // 🔥 절대경로/상대경로 상관없이 로그인 요청이면 Authorization 미부착
    if (
      config.url.includes("/user/login") || 
      config.url.includes("/user/create")
    ) {
      return config;
    }

    // 🔥 localStorage에서 문자열 토큰만 읽기
    const token = localStorage.getItem("token");

    if (token && typeof token === "string") {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// ========================================
// 🔥 응답 인터셉터 (401 처리)
// ========================================
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const msg = error.response?.data?.error;

    if (msg) {
      alert(msg);
    }

    // 🔥 토큰 만료 또는 잘못된 토큰
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/";
    }

    return Promise.reject(error);
  }
);

export default api;
