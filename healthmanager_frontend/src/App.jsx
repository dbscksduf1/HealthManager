import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Join from "./pages/Join";
import Main from "./pages/Main";
import Bmi from "./pages/Bmi";
import MyInfo from "./pages/Myinfo";
import Settings from "./pages/Settings";

import Layout from "./components/Layout";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Login />} />
        <Route path="/join" element={<Join />} />

        <Route
          path="/main"
          element={
            <Layout>
              <Main />
            </Layout>
          }
        />

        <Route
          path="/bmi"
          element={
            <Layout>
              <Bmi />
            </Layout>
          }
        />

        <Route
          path="/myinfo"
          element={
            <Layout>
              <MyInfo />
            </Layout>
          }
        />

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
