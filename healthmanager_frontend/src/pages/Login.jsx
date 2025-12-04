import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

import { page, card, input, btn, colors } from "../styles/preset";

function Login() {
  const [id, setId] = useState("");
  const [pw, setPw] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const login = async () => {
    setError("");

    if (!id || !pw) {
      setError("아이디와 비밀번호를 모두 입력해주세요.");
      return;
    }

    try {
      const res = await api.post("/user/login", {
        username: id,
        password: pw,
      });

      // 🔥 백엔드에서 token 필드로 보내기 때문에 이렇게 받아야 함
      const token = res.data.token;
      if (!token) {
        setError("서버에서 토큰을 받지 못했습니다.");
        return;
      }

      localStorage.setItem("token", token);

      navigate("/main");

    } catch (err) {
      if (err.response?.status === 401) {
        const msg =
          err.response?.data || "아이디 또는 비밀번호가 올바르지 않습니다.";
        setError(msg);
        return;
      }

      setError("로그인 중 오류가 발생했습니다.");
    }
  };

  return (
    <div style={page}>
      <div style={card}>
        <h1 style={{ marginBottom: 25, color: colors.green }}>Login</h1>

        <input
          style={input}
          placeholder="아이디"
          value={id}
          onChange={(e) => setId(e.target.value)}
        />

        <input
          style={input}
          type="password"
          placeholder="비밀번호"
          value={pw}
          onChange={(e) => setPw(e.target.value)}
        />

        {error && (
          <div style={{ color: "red", marginBottom: 10 }}>{error}</div>
        )}

        <button type="button" style={btn} onClick={login}>
          로그인
        </button>

        <button
          type="button"
          onClick={() => navigate("/join")}
          style={{
            marginTop: 15,
            background: "transparent",
            border: "none",
            color: colors.green,
            cursor: "pointer",
            fontSize: 14,
          }}
        >
          회원가입 →
        </button>
      </div>
    </div>
  );
}

export default Login;
