import { useState } from "react";
import api from "../api";
import Navbar from "../components/Navbar";
import { page, card, input, btn } from "../styles/preset";

function Bmi() {
  const [height, setHeight] = useState("");
  const [weight, setWeight] = useState("");
  const [result, setResult] = useState(null);

  const [showRoutine, setShowRoutine] = useState(false);
  const [showMeals, setShowMeals] = useState(false);

  const [routineTab, setRoutineTab] = useState("day1");
  const [mealTab, setMealTab] = useState("아침");

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

      if (msg) {
        alert(msg);
      } else {
        alert("서버 오류 또는 로그인 필요");
      }
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

            <div style={{ display: "flex", gap: 20, justifyContent: "center", marginTop: 20 }}>
              <button
                style={{ ...btn, background: showRoutine ? "#00C853" : "#ccc" }}
                onClick={() => { setShowRoutine(true); setShowMeals(false); }}
              >
                맞춤형 운동루틴
              </button>

              <button
                style={{ ...btn, background: showMeals ? "#00C853" : "#ccc" }}
                onClick={() => { setShowRoutine(false); setShowMeals(true); }}
              >
                맞춤형 식단
              </button>
            </div>

            
            {showRoutine && (
              <div style={{ marginTop: 30 }}>
                <div style={{ display: "flex", gap: 10, justifyContent: "center" }}>
                  {["day1", "day2", "day3"].map((d) => (
                    <button
                      key={d}
                      style={{
                        ...btn,
                        background: routineTab === d ? "#00C853" : "#ddd"
                      }}
                      onClick={() => setRoutineTab(d)}
                    >
                      {d === "day1" ? "Day1 (등·어깨)" :
                       d === "day2" ? "Day2 (가슴·팔)" :
                       "Day3 (하체·복근)"}
                    </button>
                  ))}
                </div>

                <div style={{
                  ...card, width: "700px",
                  margin: "30px auto", display: "flex",
                  justifyContent: "space-between"
                }}>
                  <div style={{ width: "50%", textAlign: "left" }}>
                    <h3 style={{ color: "#00C853" }}>
                      {result.routine[routineTab][0]}
                    </h3>
                    <ul>
                      {result.routine[routineTab].slice(1).map((ex, i) => (
                        <li key={i}>{ex}</li>
                      ))}
                    </ul>
                  </div>

                  <img
                    src={
                      routineTab === "day1" ? "/back_shoulder.jpg" :
                      routineTab === "day2" ? "/chest_arm.jpg" :
                      "/legs_abs.jpg"
                    }
                    alt="운동부위"
                    style={{ width: 230, height: 230, borderRadius: 15 }}
                  />
                </div>
              </div>
            )}

           
            {showMeals && (
              <div style={{ marginTop: 30 }}>
                <div style={{ display: "flex", gap: 10, justifyContent: "center" }}>
                  {["아침", "점심", "저녁"].map((m) => (
                    <button
                      key={m}
                      style={{
                        ...btn,
                        background: mealTab === m ? "#00C853" : "#ddd"
                      }}
                      onClick={() => setMealTab(m)}
                    >
                      {m}
                    </button>
                  ))}
                </div>

                <div style={{
                  ...card, width: "700px",
                  margin: "30px auto", display: "flex",
                  justifyContent: "space-between"
                }}>
                  <div style={{ width: "55%", textAlign: "left" }}>
                    <h3 style={{ color: "#00C853" }}>
                      {mealTab} 식단
                    </h3>

                    <ul>
                      {result.meals[mealTab].items.map((item, i) => (
                        <li key={i}>
                          {item.name} {item.gram}g  
                          <br />
                          → 열량 {item.cal} kcal / 탄수화물 {item.carb}g / 단백질 {item.protein}g / 지방 {item.fat}g
                        </li>
                      ))}
                    </ul>

                    <p style={{ marginTop: 10, fontWeight: "bold" }}>
                      총합: {result.meals[mealTab].total.cal} kcal  
                      / 탄수화물 {result.meals[mealTab].total.carb}g  
                      / 단백질 {result.meals[mealTab].total.protein}g  
                      / 지방 {result.meals[mealTab].total.fat}g
                    </p>
                  </div>

                  <img
                    src={
                      mealTab === "아침" ? "/breakfast.jpg" :
                      mealTab === "점심" ? "/lunch.jpg" :
                      "/dinner.jpg"
                    }
                    alt="식단 이미지"
                    style={{ width: 230, height: 230, borderRadius: 15 }}
                  />
                </div>
              </div>
            )}

          </div>
        )}
      </div>
    </div>
  );
}

export default Bmi;
