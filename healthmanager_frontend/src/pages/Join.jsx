import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

import { page, card, input, btn, colors } from "../styles/preset";

function Join() {
  const [id, setId] = useState("");
  const [pw, setPw] = useState("");
  const [age, setAge] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  const join = async () => {
    setError("");

    if (!id || !pw || !age) {
      setError("모든 항목을 입력해주세요.");
      return;
    }

    try {
      await api.post("/user/create", {
        username: id,
        password: pw,
        age: parseInt(age),
      });

      alert("회원가입 성공! 로그인해주세요.");
      navigate("/");

    } catch (err) {
      const msg = err.response?.data?.error || "회원가입 실패. 아이디 중복 또는 서버 오류입니다.";
      setError(msg);
      alert(msg);
    }
  };

  return (
    <div style={page}>
      <div style={card}>
        <h1 style={{ marginBottom: 25, color: colors.green }}>회원가입</h1>

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

        <input
          style={input}
          type="number"
          placeholder="나이"
          value={age}
          onChange={(e) => setAge(e.target.value)}
        />

        {error && (
          <div style={{ color: "red", marginBottom: 10 }}>{error}</div>
        )}

        <button style={btn} onClick={join}>
          가입하기
        </button>

        <button
          onClick={() => navigate("/")}
          style={{
            marginTop: 15,
            background: "transparent",
            border: "none",
            color: colors.green,
            cursor: "pointer",
            fontSize: 14,
          }}
        >
          로그인으로 돌아가기 →
        </button>
      </div>
    </div>
  );
}

export default Join;
