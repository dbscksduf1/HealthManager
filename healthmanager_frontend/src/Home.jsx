import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();

  return (
    <div
      style={{
        width: "100vw",
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        background: "#e5e7eb",
      }}
    >
      <div
        style={{
          width: 400,
          padding: 40,
          borderRadius: 10,
          background: "white",
          textAlign: "center",
          boxShadow: "0 4px 20px rgba(0,0,0,0.1)",
        }}
      >
        <h1 style={{ marginBottom: 20 }}>홈 화면</h1>
        <p style={{ marginBottom: 30 }}>로그인 성공! 원하는 기능을 선택하세요.</p>

        <button
          onClick={() => navigate("/bmi")}
          style={btnStyle}
        >
          BMI / 운동 루틴 추천
        </button>

        <button
          onClick={() => navigate("/diet")}
          style={btnStyle}
        >
          오늘의 식단
        </button>

        <button
          onClick={() => navigate("/profile")}
          style={btnStyle}
        >
          내 정보 수정
        </button>
      </div>
    </div>
  );
}

const btnStyle = {
  width: "100%",
  padding: 12,
  marginBottom: 15,
  background: "#4f46e5",
  color: "white",
  border: "none",
  borderRadius: 6,
  cursor: "pointer",
  fontSize: 16,
};

export default Home;
