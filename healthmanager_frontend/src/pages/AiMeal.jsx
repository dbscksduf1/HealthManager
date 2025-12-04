import { useEffect, useState } from "react";
import api from "../api";
import Navbar from "../components/Navbar";
import { page, card, btn } from "../styles/preset";

function AiMeal() {
  const [meal, setMeal] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadMeal = async () => {
    try {
      const res = await api.post("/ai/assistant", { type: "meal" });
      setMeal(res.data.result); // result는 객체 형태로 받아온다고 가정
    } catch (e) {
      setMeal(null);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadMeal();
  }, []);

  return (
    <div style={{ ...page, display: "flex", flexDirection: "column", alignItems: "center" }}>
      <Navbar />

      <div style={{ ...card, width: "900px", marginTop: 40, padding: 30 }}>
        <h1 style={{ color: "#00C853", marginBottom: 20 }}>AI 추천 식단 🍽</h1>

        {loading && <p>AI가 식단을 생성 중입니다...</p>}

        {!loading && !meal && (
          <p>식단 정보를 불러올 수 없습니다.</p>
        )}

        {!loading && meal && (
          <div style={{ marginTop: 20 }}>
            
            {/* 아침 */}
            <div style={{ ...card, width: "100%", marginBottom: 20, padding: 20 }}>
              <h2 style={{ color: "#00C853" }}>🍳 아침</h2>
              <p style={{ fontWeight: "bold", marginTop: 5 }}>{meal.breakfast.name}</p>
              <p style={{ marginTop: 5 }}>{meal.breakfast.desc}</p>
              <p style={{ marginTop: 10 }}>
                🔹 열량 {meal.breakfast.cal} kcal / 탄 {meal.breakfast.carb}g /
                단 {meal.breakfast.protein}g / 지 {meal.breakfast.fat}g
              </p>
              <p style={{ marginTop: 10, fontStyle: "italic" }}>{meal.breakfast.reason}</p>
            </div>

            {/* 점심 */}
            <div style={{ ...card, width: "100%", marginBottom: 20, padding: 20 }}>
              <h2 style={{ color: "#00C853" }}>🍱 점심</h2>
              <p style={{ fontWeight: "bold", marginTop: 5 }}>{meal.lunch.name}</p>
              <p style={{ marginTop: 5 }}>{meal.lunch.desc}</p>
              <p style={{ marginTop: 10 }}>
                🔹 열량 {meal.lunch.cal} kcal / 탄 {meal.lunch.carb}g /
                단 {meal.lunch.protein}g / 지 {meal.lunch.fat}g
              </p>
              <p style={{ marginTop: 10, fontStyle: "italic" }}>{meal.lunch.reason}</p>
            </div>

            {/* 저녁 */}
            <div style={{ ...card, width: "100%", marginBottom: 20, padding: 20 }}>
              <h2 style={{ color: "#00C853" }}>🍗 저녁</h2>
              <p style={{ fontWeight: "bold", marginTop: 5 }}>{meal.dinner.name}</p>
              <p style={{ marginTop: 5 }}>{meal.dinner.desc}</p>
              <p style={{ marginTop: 10 }}>
                🔹 열량 {meal.dinner.cal} kcal / 탄 {meal.dinner.carb}g /
                단 {meal.dinner.protein}g / 지 {meal.dinner.fat}g
              </p>
              <p style={{ marginTop: 10, fontStyle: "italic" }}>{meal.dinner.reason}</p>
            </div>

          </div>
        )}

      </div>
    </div>
  );
}

export default AiMeal;
