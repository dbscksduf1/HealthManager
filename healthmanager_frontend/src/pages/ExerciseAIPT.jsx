import { useState } from "react";
import api from "../api";
import exerciseCategoryMap from "../data/exerciseCategoryMap";
import exerciseImages from "../data/exerciseImages";
import "../styles/exercise.css";

function ExerciseAIPT() {
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedExercise, setSelectedExercise] = useState("");
  const [result, setResult] = useState("");
  const [loading, setLoading] = useState(false);

  const categories = {
    등: ["랫풀다운", "풀업", "T바 로우", "시티드 로우", "바벨 벤트오버 로우", "덤벨 원암 로우"],
    가슴: ["체스트 프레스 머신", "덤벨 벤치프레스", "케이블 크로스오버", "딥스", "인클라인 벤치프레스", "펙덱 플라이"],
    어깨: ["오버헤드프레스", "사이드 레터럴 레이즈", "벤트오버 레터럴 레이즈", "프론트 레이즈", "머신 숄더 프레스", "페이스풀"],
    하체: ["바벨 스쿼트", "레그프레스", "런지", "레그 익스텐션", "레그 컬", "루마니안 데드리프트"],
    이두: ["바벨 컬", "덤벨 컬", "해머 컬", "케이블 스트레이트바 컬", "프리처 컬", "얼터네이트 컬"],
    삼두: ["케이블 푸시다운", "덤벨 킥백", "스컬크러셔", "딥스(삼두)", "오버헤드 익스텐션", "클로즈그립 벤치프레스"],
    복근: ["크런치", "레그레이즈", "행잉 레그레이즈", "플랭크", "러시안 트위스트", "케이블 크런치"],
  };

  const handleExerciseClick = async (exercise) => {
    setSelectedExercise(exercise);
    setLoading(true);
    setResult("");

    try {
      const res = await api.post("/ai/exercise-detail", { exercise });
      setResult(res.data.result);
    } catch {
      setResult("AI 설명을 가져올 수 없습니다.");
    }
    setLoading(false);
  };

  const imageCategory = exerciseCategoryMap[selectedExercise];
  const imagePath = imageCategory ? exerciseImages[imageCategory] : null;

  return (
    <div className="exercise-page-wrapper">
      <div className="exercise-container">
        <div className="exercise-card">

          <h1 className="exercise-title">운동 자세</h1>

          <h3 className="exercise-subtitle">운동할 부위를 선택해주세요.</h3>
          <div className="exercise-buttons">
            {Object.keys(categories).map((cat) => (
              <button
                key={cat}
                className={`exercise-btn ${selectedCategory === cat ? "active" : ""}`}
                onClick={() => setSelectedCategory(cat)}
              >
                {cat}
              </button>
            ))}
          </div>

          {selectedCategory ? (
            <>
              <h3 className="exercise-subtitle">운동 선택</h3>
              <div className="exercise-buttons">
                {categories[selectedCategory].map((ex) => (
                  <button
                    key={ex}
                    className={`exercise-btn ${selectedExercise === ex ? "active" : ""}`}
                    onClick={() => handleExerciseClick(ex)}
                  >
                    {ex}
                  </button>
                ))}
              </div>
            </>
          ) : (
            <div className="exercise-placeholder">운동 부위를 먼저 선택하세요.</div>
          )}

          {loading && <p className="exercise-placeholder">AI가 설명 생성 중...</p>}

          {result && (
            <div className="exercise-content">
              {imagePath && (
                <img src={imagePath} className="exercise-image" alt="exercise" />
              )}
              <div className="exercise-info">
                <h3>{selectedExercise}</h3>
                <p className="exercise-text">{result}</p>
              </div>
            </div>
          )}

        </div>
      </div>
    </div>
  );
}

export default ExerciseAIPT;
