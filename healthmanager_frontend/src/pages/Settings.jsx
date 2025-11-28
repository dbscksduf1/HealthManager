import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../api";
import { page, card, btn, colors } from "../styles/preset";

function Settings() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [dark, setDark] = useState(false);

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
      } catch (err) {
        alert(err.response?.data?.error || "로그인이 필요합니다.");
        localStorage.removeItem("token");
        navigate("/");
      }
    };
    loadUser();
  }, []);

  const deleteAccount = async () => {
    if (!window.confirm("정말로 계정을 삭제하시겠습니까?")) return;

    try {
      await api.delete(`/user/delete/${user.id}`);
      alert("계정이 삭제되었습니다.");
      localStorage.removeItem("token");
      navigate("/");
    } catch (err) {
      const msg = err.response?.data?.error || "삭제 실패했습니다.";
      alert(msg);
    }
  };

  const clearCache = () => {
    localStorage.clear();
    alert("캐시가 초기화되었습니다.");
  };

  return (
    <div style={{ ...page, background: dark ? "#1f1f1f" : colors.bg }}>
      <Navbar />

      <div style={{ marginTop: 60 }}>
        <div style={{ ...card, background: dark ? "#2a2a2a" : colors.white, color: dark ? "#fff" : "#222" }}>

          <h1 style={{ marginBottom: 25, color: colors.green }}>
            설정
          </h1>

          <div style={section}>
            <h3>계정 정보</h3>
            {user ? (
              <p>현재 로그인: <b>{user.username}</b></p>
            ) : (
              <p>로딩중...</p>
            )}
          </div>

          <div style={section}>
            <h3>화면 모드</h3>
            <button
              style={btn}
              onClick={() => setDark(!dark)}
            >
              {dark ? "라이트 모드" : "다크 모드"}
            </button>
          </div>

          <div style={section}>
            <h3>캐시 초기화</h3>
            <button style={btn} onClick={clearCache}>
              캐시 삭제
            </button>
          </div>

          <div style={section}>
            <h3>계정 관리</h3>
            <button style={dangerBtn} onClick={deleteAccount}>
              계정 삭제
            </button>
          </div>

          <button style={{ ...btn, marginTop: 20 }} onClick={() => navigate("/main")}>
            메인으로 돌아가기
          </button>
        </div>
      </div>
    </div>
  );
}

const section = {
  marginBottom: 30,
};

const dangerBtn = {
  ...btn,
  background: "#d93025",
};

export default Settings;
