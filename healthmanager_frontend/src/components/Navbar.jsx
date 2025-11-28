import { useNavigate } from "react-router-dom";
import { colors } from "../styles/preset";

function Navbar() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    alert("로그아웃 되었습니다.");
    navigate("/");
  };

  const goMain = () => {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("로그인이 필요합니다.");
      navigate("/");
      return;
    }
    navigate("/main");
  };

  return (
    <div
      style={{
        width: "100%",
        padding: "15px 30px",
        background: colors.white,
        borderBottom: "1px solid #eaeaea",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        position: "fixed",
        top: 0,
        zIndex: 999,
      }}
    >
      <div
        style={{
          fontWeight: "bold",
          fontSize: 22,
          color: colors.green,
          cursor: "pointer",
        }}
        onClick={goMain}
      >
        HealthManager
      </div>

      <div style={{ display: "flex", gap: 20, alignItems: "center" }}>
        <button style={navBtn} onClick={() => navigate("/main")}>홈</button>
        <button style={navBtn} onClick={() => navigate("/bmi")}>BMI</button>
        <button style={navBtn} onClick={() => navigate("/myinfo")}>내 정보</button>
        <button style={navBtn} onClick={() => navigate("/settings")}>설정</button>

        <button
          onClick={logout}
          style={{
            ...navBtn,
            color: colors.green,
            border: `1px solid ${colors.green}`,
            padding: "6px 14px",
            borderRadius: 6,
            fontWeight: "bold",
          }}
        >
          로그아웃
        </button>
      </div>
    </div>
  );
}

const navBtn = {
  background: "transparent",
  border: "none",
  color: colors.text,
  fontSize: 15,
  cursor: "pointer",
};

export default Navbar;
