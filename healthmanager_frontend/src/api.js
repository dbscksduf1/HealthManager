import axios from "axios";

const api = axios.create({
  baseURL: "https://healthmanager-backend.onrender.com/",
  withCredentials: true,
});

// 🔥 요청 인터셉터 (JWT 자동첨부)
api.interceptors.request.use(
  (config) => {
    // 로그인 요청에는 Authorization 붙이면 안 됨
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

// 🔥 응답 인터셉터 (401일 때 강제 로그아웃 금지)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const msg = error.response?.data?.error;

    if (msg) {
      alert(msg);
    }

    // ❗ 절대 토큰 자동 삭제 + 강제 이동하지 말 것
    // Render는 첫 요청이 401 나오는 경우가 있어서
    // 이 코드 때문에 정상 로그인도 실패로 처리됨
    //
    // if (error.response?.status === 401) {
    //   localStorage.removeItem("token");
    //   window.location.href = "/";
    // }

    return Promise.reject(error);
  }
);

export default api;
