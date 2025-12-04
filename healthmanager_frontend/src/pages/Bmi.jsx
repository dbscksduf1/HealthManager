import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import Navbar from "../components/Navbar";
import { page, card, input, btn } from "../styles/preset";

function Bmi() {
  const navigate = useNavigate();

  const [height, setHeight] = useState("");
  const [weight, setWeight] = useState("");
  const [result, setResult] = useState(null);

  const calculate = async () => {
    setResult(null);

    if (!height || !weight) {
      alert("키와 몸무게를 입력해주세요.");
      return;
    }
    if (isNaN(height) || isNaN(weight)) {
      alert("숫자만 입력해주세요.");
      return;
    }

    try {
      const res = await api.get("/health/status", {
        params: { height, weight }
      });

      if (res.status !== 200) {
        alert(res.data.error || "오류가 발생했습니다.");
        return;
      }

      setResult(res.data);
    } catch (err) {
      console.error(err);

      const msg = err.response?.data?.error;
      if (msg) alert(msg);
      else alert("서버 오류 또는 로그인 필요");
    }
  };

  return (
    <div style={{ ...page, display: "flex", flexDirection: "column", alignItems: "center" }}>
      <Navbar />

      <div style={{ ...card, width: "900px", marginTop: 40, textAlign: "center" }}>
        <h1 style={{ color: "#00C853", marginBottom: 30 }}>BMI 계산하기</h1>

        <div style={{ display: "flex", justifyContent: "center", gap: 10 }}>
          <input
            style={{ ...input, width: 200 }}
            placeholder="키(cm)"
            value={height}
            onChange={(e) => setHeight(e.target.value)}
          />
          <input
            style={{ ...input, width: 200 }}
            placeholder="몸무게(kg)"
            value={weight}
            onChange={(e) => setWeight(e.target.value)}
          />
          <button style={btn} onClick={calculate}>BMI 계산하기</button>
        </div>

        {result && (
          <div style={{ marginTop: 30 }}>
            <h2>🔥 결과</h2>
            <p>BMI: {result.bmi.toFixed(2)}</p>
            <p>목표: {result.goal}</p>

            {/* AI PT 코멘트 */}
            <div
              style={{
                marginTop: 20,
                background: "#f6f6f6",
                padding: "15px 20px",
                borderRadius: 10,
                width: "80%",
                marginLeft: "auto",
                marginRight: "auto",
                textAlign: "left",
                border: "1px solid #ccc"
              }}
            >
              <p style={{ fontWeight: "bold", color: "#00C853", marginBottom: 8 }}>
                AI PT 👟
              </p>
              <p style={{ whiteSpace: "pre-line", lineHeight: "1.5" }}>
                {result.aiComment || "AI 코멘트 로딩 실패"}
              </p>
            </div>

            {/* 새로운 버튼 두 개 */}
            <div style={{ display: "flex", justifyContent: "center", gap: 20, marginTop: 30 }}>
              <button
                style={{ ...btn, width: 250, background: "#00C853" }}
                onClick={() => navigate("/exercise-ai")}
              >
                운동 자세/설명 보러가기
              </button>

              <button
                style={{ ...btn, width: 250, background: "#00C853" }}
                onClick={() => navigate("/assistant")}
              >
                PT 챗봇 보러가기
              </button>
            </div>

          </div>
        )}
      </div>
    </div>
  );
}

export default Bmi;
