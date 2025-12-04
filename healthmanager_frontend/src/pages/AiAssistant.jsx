import { useState } from "react";
import api from "../api";
import "../styles/chat.css";

function AiAssistant() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");

  const addMessage = (text, sender) => {
    setMessages((prev) => [...prev, { text, sender }]);
  };

  const callAi = async (type) => {
    addMessage(type.label, "user");

    try {
      const res = await api.post("/ai/assistant", { type: type.value });
      addMessage(res.data.result, "bot");
    } catch {
      addMessage("서버 오류가 발생했습니다.", "bot");
    }
  };

  const sendInput = async () => {
    if (!input.trim()) return;

    addMessage(input, "user");
    const question = input;
    setInput("");

    try {
      const res = await api.post("/ai/chat", { message: question });
      addMessage(res.data.result, "bot");
    } catch {
      addMessage("AI 응답 중 오류가 발생했습니다.", "bot");
    }
  };

  const quickButtons = [
    { label: "오늘의 운동 루틴", value: "routine" },
    { label: "AI 추천 식단", value: "meal" },
    { label: "초보자 팁", value: "beginner" },
    { label: "체지방 감량 팁", value: "fatloss" },
    { label: "벌크업 조언", value: "bulk" }
  ];

  return (
  <div
    style={{
      position: "absolute",
      left: "50%",
      top: "120px",         // 🔥 여기 추가 (위쪽으로 올릴 위치)
      transform: "translateX(-50%)",
      width: "750px"
    }}
  >

      <div className="chat-container">

        <h1 className="chat-title">PT 챗봇</h1>

        <div className="chat-box">
          {messages.map((m, i) => (
            <div key={i} className={`chat-message ${m.sender}`}>
              <div className="bubble">{m.text}</div>
            </div>
          ))}
        </div>

        <div className="input-area">
          <input
            className="chat-input"
            placeholder="궁금한 점을 질문해보세요!"
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
          <button className="send-btn" onClick={sendInput}>확인</button>
        </div>

        <div className="quick-buttons">
          {quickButtons.map((b) => (
            <button
              key={b.value}
              className="quick-btn"
              onClick={() => callAi(b)}
            >
              {b.label}
            </button>
          ))}
        </div>

      </div>
    </div>
  );
}

export default AiAssistant;
