import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Join from "./pages/Join";
import Main from "./pages/Main";
import Bmi from "./pages/Bmi";
import MyInfo from "./pages/Myinfo";
import Settings from "./pages/Settings";

import ExerciseAIPT from "./pages/ExerciseAIPT";
import AiAssistant from "./pages/AiAssistant";
import AiMeal from "./pages/AiMeal";

import Layout from "./components/Layout";

console.log("FRONT BUILD VERSION: 2025-12-05-001");

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* 로그인 / 회원가입 */}
        <Route path="/" element={<Login />} />
        <Route path="/join" element={<Join />} />

        {/* 메인 */}
        <Route
          path="/main"
          element={
            <Layout>
              <Main />
            </Layout>
          }
        />

        {/* BMI 계산 */}
        <Route
          path="/bmi"
          element={
            <Layout>
              <Bmi />
            </Layout>
          }
        />

        {/* AI 운동 자세 설명 */}
        <Route
          path="/exercise-ai"
          element={
            <Layout>
              <ExerciseAIPT />
            </Layout>
          }
        />

        {/* AI 헬스 챗봇 */}
        <Route
          path="/assistant"
          element={
            <Layout>
              <AiAssistant />
            </Layout>
          }
        />

        {/* AI 추천 식단 */}
        <Route
          path="/ai-meal"
          element={
            <Layout>
              <AiMeal />
            </Layout>
          }
        />

        {/* 내 정보 */}
        <Route
          path="/myinfo"
          element={
            <Layout>
              <MyInfo />
            </Layout>
          }
        />

        {/* 설정 */}
        <Route
          path="/settings"
          element={
            <Layout>
              <Settings />
            </Layout>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
