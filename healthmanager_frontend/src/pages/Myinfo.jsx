import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../api";
import { page, card, input, btn, colors } from "../styles/preset";

function MyInfo() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [age, setAge] = useState("");
  const [password, setPassword] = useState("");
  const [msg, setMsg] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("로그인이 필요합니다.");
      navigate("/");
    }
  }, []);

  useEffect(() => {
    const loadUser = async () => {
      try {
        const res = await api.get("/user/me");
        setUser(res.data);
        setAge(res.data.age);
      } catch (e) {
        alert(e.response?.data?.error || "로그인이 필요합니다.");
        localStorage.removeItem("token");
        navigate("/");
      }
    };
    loadUser();
  }, []);

  const updateUser = async () => {
    if (!user) return;

    try {
      await api.put(`/user/update/${user.id}`, {
        username: user.username,
        age: age,
        password: password || "",
      });

      setMsg("정보가 성공적으로 수정되었습니다.");
      setPassword("");

    } catch (e) {
      const errMsg = e.response?.data?.error || "정보 수정 실패. 서버 오류 또는 잘못된 값입니다.";
      setMsg(errMsg);
      alert(errMsg);
    }
  };

  if (!user) return <div>로딩중...</div>;

  return (
    <div style={page}>
      <Navbar />

      <div style={{ marginTop: 60 }}>
        <div style={card}>
          <h1 style={{ marginBottom: 20, color: colors.green }}>내 정보</h1>

          {msg && (
            <div style={{ color: colors.green, marginBottom: 15, fontWeight: "bold" }}>
              {msg}
            </div>
          )}

          <div style={fieldWrap}>
            <label style={label}>아이디</label>
            <input
              value={user.username}
              readOnly
              style={inputDisabled}
            />
          </div>

          <div style={fieldWrap}>
            <label style={label}>나이</label>
            <input
              style={input}
              value={age}
              onChange={(e) => setAge(e.target.value)}
            />
          </div>

          <div style={fieldWrap}>
            <label style={label}>새 비밀번호 (선택)</label>
            <input
              type="password"
              style={input}
              placeholder="변경 시에만 입력하세요"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button onClick={updateUser} style={{ ...btn, marginTop: 25 }}>
            저장하기
          </button>
        </div>
      </div>
    </div>
  );
}

const fieldWrap = {
  display: "flex",
  flexDirection: "column",
  marginBottom: 10,
};

const label = {
  fontWeight: "bold",
  marginBottom: 6,
};

const inputDisabled = {
  ...input,
  background: "#f1f1f1",
  color: "#888",
};

export default MyInfo;
