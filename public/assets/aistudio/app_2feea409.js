/* GorphinAI Production Frontend: Auth, Chats, 4 Tariffs (PayGate.to), <think>, and 3 Languages (EN/UK/RU) */

const I18N = {
  ru: {
    brandSubtitle: "Разработано украинской командой Gorphin",
    brandShort: "команда Gorphin",
    loginTab: "Вход",
    registerTab: "Регистрация",
    usernameLabel: "Имя пользователя",
    emailLabel: "Email",
    passwordLabel: "Пароль",
    loginBtn: "Войти",
    registerBtn: "Создать аккаунт",
    newChat: "+ Новый чат",
    upgradeBtn: "Тарифы",
    qMessages: "Сообщения (24ч / 4ч)",
    qThink: "Thinking (<think>)",
    qWeb: "Веб / URL / GitHub",
    tariffsTitle: "Тарифы",
    tariffsSubtitle: "Безопасная оплата картой или криптовалютой через PayGate.to",
    welcomeTitle: "Чем могу помочь сегодня?",
    welcomeSub: "Универсальный ИИ-ассистент с режимом размышлений (<think>), поиском в интернете и анализом кода/URL.",
    inputPlaceholder: "Напишите сообщение или вставьте ссылку / GitHub URL...",
    sendBtn: "Отправить",
    settingsTitle: "Настройки",
    interfaceLang: "Язык интерфейса и ответов",
    savedMsg: "Сохранено!",
    logoutBtn: "Выйти",
    saveBtn: "Сохранить",
    monthly: "Помесячно",
    yearly: "На год (выгоднее)",
    perMo: "/ мес",
    perYr: "/ год",
    currentPlan: "Текущий тариф",
    choosePlan: "Подключить",
    renewBtn: "Продлить (+30 дней)",
    renewPrompt: (name, hrs) => `⏳ Подписка ${name} истекает через ${hrs} ч. Продлите сейчас (+30 дней к остатку).`,
    thinkAuto: "🧠 Think: Авто",
    thinkOn: "🧠 Think: Вкл",
    thinkOff: "🧠 Think: Выкл",
    webOn: "🌐 Интернет: Вкл",
    webOff: "🌐 Интернет: Выкл",
    searching: "🔍 Ищу информацию в интернете...",
    thinkingLive: "💭 Размышления GorphinAI...",
    thinkingDone: "💭 Показать размышления (<think>)",
    deleteConfirm: "Удалить этот чат?",
    planRoles: {
      free: "Витрина и тест возможностей без карты и рекламы",
      basic: "Массовый рабочий тариф на каждый день",
      plus: "Пакет для разработчиков и глубокого кода",
      pro: "Максимальный лимит + прямой канал Gorphin Q&A",
    },
    planFeat: {
      msg: (d, h) => `${d} сообщений / сутки (до ${h} за 4 часа)`,
      think: (d, t) => `${d} Thinking-запросов / сутки (до ${t} think-токенов)`,
      web: (d, u) => `${d} веб-поисков / сутки (до ${u} URL / файлов GitHub)`,
      proExtra: "Приоритетный парсинг + прямой канал Gorphin Q&A",
    },
  },
  uk: {
    brandSubtitle: "Розроблено українською командою Gorphin",
    brandShort: "команда Gorphin",
    loginTab: "Вхід",
    registerTab: "Реєстрація",
    usernameLabel: "Ім'я користувача",
    emailLabel: "Email",
    passwordLabel: "Пароль",
    loginBtn: "Увійти",
    registerBtn: "Створити акаунт",
    newChat: "+ Новий чат",
    upgradeBtn: "Тарифи",
    qMessages: "Повідомлення (24г / 4г)",
    qThink: "Thinking (<think>)",
    qWeb: "Веб / URL / GitHub",
    tariffsTitle: "Тарифи",
    tariffsSubtitle: "Безпечна оплата карткою або криптовалютою через PayGate.to",
    welcomeTitle: "Чим можу допомогти сьогодні?",
    welcomeSub: "Універсальний ШІ-асистент із режимом міркувань (<think>), пошуком в інтернеті та аналізом коду/URL.",
    inputPlaceholder: "Напишіть повідомлення або вставте посилання / GitHub URL...",
    sendBtn: "Надіслати",
    settingsTitle: "Налаштування",
    interfaceLang: "Мова інтерфейсу та відповідей",
    savedMsg: "Збережено!",
    logoutBtn: "Вийти",
    saveBtn: "Зберегти",
    monthly: "Щомісяця",
    yearly: "На рік (вигідніше)",
    perMo: "/ міс",
    perYr: "/ рік",
    currentPlan: "Поточний тариф",
    choosePlan: "Підключити",
    renewBtn: "Продовжити (+30 днів)",
    renewPrompt: (name, hrs) => `⏳ Підписка ${name} закінчується через ${hrs} год. Продовжіть зараз (+30 днів до залишку).`,
    thinkAuto: "🧠 Think: Авто",
    thinkOn: "🧠 Think: Увімк",
    thinkOff: "🧠 Think: Вимк",
    webOn: "🌐 Інтернет: Увімк",
    webOff: "🌐 Інтернет: Вимк",
    searching: "🔍 Шукаю інформацію в інтернеті...",
    thinkingLive: "💭 Міркування GorphinAI...",
    thinkingDone: "💭 Показати міркування (<think>)",
    deleteConfirm: "Видалити цей чат?",
    planRoles: {
      free: "Вітрина і тест можливостей без картки та реклами",
      basic: "Масовий робочий тариф на щодень",
      plus: "Пакет для розробників і глибокого коду",
      pro: "Максимальний ліміт + прямий канал Gorphin Q&A",
    },
    planFeat: {
      msg: (d, h) => `${d} повідомлень / добу (до ${h} за 4 години)`,
      think: (d, t) => `${d} Thinking-запитів / добу (до ${t} think-токенів)`,
      web: (d, u) => `${d} веб-пошуків / добу (до ${u} URL / файлів GitHub)`,
      proExtra: "Пріоритетний парсинг + прямий канал Gorphin Q&A",
    },
  },
  en: {
    brandSubtitle: "Developed by the Ukrainian team Gorphin",
    brandShort: "by Gorphin Team",
    loginTab: "Sign In",
    registerTab: "Sign Up",
    usernameLabel: "Username",
    emailLabel: "Email",
    passwordLabel: "Password",
    loginBtn: "Sign In",
    registerBtn: "Create Account",
    newChat: "+ New Chat",
    upgradeBtn: "Plans",
    qMessages: "Messages (24h / 4h)",
    qThink: "Thinking (<think>)",
    qWeb: "Web / URL / GitHub",
    tariffsTitle: "Pricing Plans",
    tariffsSubtitle: "Secure card & crypto checkout powered by PayGate.to",
    welcomeTitle: "How can I help you today?",
    welcomeSub: "General-purpose AI assistant with <think> reasoning, live web search, and URL/GitHub code analysis.",
    inputPlaceholder: "Message GorphinAI or paste a URL / GitHub file link...",
    sendBtn: "Send",
    settingsTitle: "Settings",
    interfaceLang: "Interface & response language",
    savedMsg: "Saved!",
    logoutBtn: "Log Out",
    saveBtn: "Save",
    monthly: "Monthly",
    yearly: "Yearly (Save)",
    perMo: "/ mo",
    perYr: "/ yr",
    currentPlan: "Current Plan",
    choosePlan: "Upgrade",
    renewBtn: "Renew (+30 days)",
    renewPrompt: (name, hrs) => `⏳ Your ${name} plan expires in ${hrs}h. Renew now (+30 days added to remaining time).`,
    thinkAuto: "🧠 Think: Auto",
    thinkOn: "🧠 Think: On",
    thinkOff: "🧠 Think: Off",
    webOn: "🌐 Web: On",
    webOff: "🌐 Web: Off",
    searching: "🔍 Searching the web...",
    thinkingLive: "💭 GorphinAI is thinking...",
    thinkingDone: "💭 View reasoning (<think>)",
    deleteConfirm: "Delete this chat?",
    planRoles: {
      free: "Test drive with no card and no ads",
      basic: "Everyday productivity plan",
      plus: "Built for developers & deep coding",
      pro: "Maximum limits + direct Gorphin Q&A channel",
    },
    planFeat: {
      msg: (d, h) => `${d} messages / day (up to ${h} per 4h)`,
      think: (d, t) => `${d} Thinking queries / day (up to ${t} think-tokens)`,
      web: (d, u) => `${d} web/URL parses / day (up to ${u} URLs / GitHub files)`,
      proExtra: "Priority parsing + direct Gorphin Q&A channel",
    },
  },
};

// Automatically talk to same-origin production API (or :8000 if static dev server is on :3000)
const API_BASE = "https://cloud-worker.gorphinai.pp.ua";
  window.location.port === "3000"
    ? `${window.location.protocol}//${window.location.hostname}:8000`
    : "";

const TOKEN_KEY = "gorphin_auth_token";

if (window.location.hash.startsWith("#oauth_token=")) {
  const oauthTok = decodeURIComponent(window.location.hash.slice("#oauth_token=".length));
  if (oauthTok) {
    localStorage.setItem(TOKEN_KEY, oauthTok);
    history.replaceState(null, "", window.location.pathname + window.location.search);
  }
}
const LANG_KEY = "gorphin_ui_lang";

let authToken = localStorage.getItem(TOKEN_KEY) || "";
let uiLang = localStorage.getItem(LANG_KEY) || "en";
let authMode = "login";
let currentUser = null;
let plansCatalog = null;
let billingPeriod = "monthly";
let chats = [];
let activeChatId = null;
let thinkMode = "auto"; // auto | on | off
let webEnabled = false;
let busy = false;

// DOM elements
const authScreen = document.getElementById("authScreen");
const mainApp = document.getElementById("mainApp");
const authLangSelect = document.getElementById("authLangSelect");
const tabLogin = document.getElementById("tabLogin");
const tabRegister = document.getElementById("tabRegister");
const authForm = document.getElementById("authForm");
const usernameField = document.getElementById("usernameField");
const authUsername = document.getElementById("authUsername");
const authEmail = document.getElementById("authEmail");
const authPassword = document.getElementById("authPassword");
const authError = document.getElementById("authError");
const authSubmitBtn = document.getElementById("authSubmitBtn");

const sidebar = document.getElementById("sidebar");
const sidebarBackdrop = document.getElementById("sidebarBackdrop");
const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const chatList = document.getElementById("chatList");
const newChatBtn = document.getElementById("newChatBtn");
const currentChatTitle = document.getElementById("currentChatTitle");
const chatLog = document.getElementById("chatLog");
const chatForm = document.getElementById("chatForm");
const promptInput = document.getElementById("promptInput");
const sendBtn = document.getElementById("sendBtn");
const thinkBtn = document.getElementById("thinkBtn");
const webBtn = document.getElementById("webBtn");

const userPlanBadge = document.getElementById("userPlanBadge");
const renewBanner = document.getElementById("renewBanner");
const renewText = document.getElementById("renewText");
const renewBtn = document.getElementById("renewBtn");
const qMsgVal = document.getElementById("qMsgVal");
const qThinkVal = document.getElementById("qThinkVal");
const qWebVal = document.getElementById("qWebVal");
const sidebarUsername = document.getElementById("sidebarUsername");
const sidebarEmail = document.getElementById("sidebarEmail");

const settingsModal = document.getElementById("settingsModal");
const settingsLangSelect = document.getElementById("settingsLangSelect");
const settingsUsername = document.getElementById("settingsUsername");
const saveSettingsBtn = document.getElementById("saveSettingsBtn");
const logoutBtn = document.getElementById("logoutBtn");
const settingsSavedMsg = document.getElementById("settingsSavedMsg");

const plansModal = document.getElementById("plansModal");
const plansGrid = document.getElementById("plansGrid");
const plansError = document.getElementById("plansError");
const billMonthly = document.getElementById("billMonthly");
const billYearly = document.getElementById("billYearly");

function t(key) {
  return (I18N[uiLang] && I18N[uiLang][key]) || I18N.ru[key] || key;
}

function applyTranslations() {
  document.documentElement.lang = uiLang;
  authLangSelect.value = uiLang;
  settingsLangSelect.value = uiLang;
  document.querySelectorAll("[data-i18n]").forEach((el) => {
    const k = el.getAttribute("data-i18n");
    if (k) el.textContent = t(k);
  });
  document.querySelectorAll("[data-i18n-placeholder]").forEach((el) => {
    const k = el.getAttribute("data-i18n-placeholder");
    if (k) el.setAttribute("placeholder", t(k));
  });
  authSubmitBtn.textContent = authMode === "login" ? t("loginBtn") : t("registerBtn");
  syncToolPills();
  if (plansCatalog) renderPlans();
}

function syncToolPills() {
  thinkBtn.className = "pill";
  if (thinkMode === "auto") {
    thinkBtn.classList.add("pill-auto");
    thinkBtn.textContent = t("thinkAuto");
  } else if (thinkMode === "on") {
    thinkBtn.classList.add("active");
    thinkBtn.textContent = t("thinkOn");
  } else {
    thinkBtn.textContent = t("thinkOff");
  }
  webBtn.className = webEnabled ? "pill active" : "pill";
  webBtn.textContent = webEnabled ? t("webOn") : t("webOff");
}

async function apiFetch(path, options = {}) {
  const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
  if (authToken) headers["X-Session-Token"] = authToken;
  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });
  if (res.status === 401 && path !== "/api/auth/login") {
    setLoggedOut();
    throw new Error("Session expired");
  }
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.detail || `HTTP ${res.status}`);
  return data;
}

// --- Auth ---
function setAuthMode(mode) {
  authMode = mode;
  tabLogin.classList.toggle("active", mode === "login");
  tabRegister.classList.toggle("active", mode === "register");
  usernameField.classList.toggle("hidden", mode === "login");
  authError.classList.add("hidden");
  authSubmitBtn.textContent = mode === "login" ? t("loginBtn") : t("registerBtn");
}

tabLogin.onclick = () => setAuthMode("login");
tabRegister.onclick = () => setAuthMode("register");
document.getElementById("oauthGoogleBtn")?.addEventListener("click", () => {
  window.location.href = `${API_BASE}/api/auth/oauth/google?frontend=${encodeURIComponent(window.location.origin + "/chat")}`;
});
document.getElementById("oauthGithubBtn")?.addEventListener("click", () => {
  window.location.href = `${API_BASE}/api/auth/oauth/github?frontend=${encodeURIComponent(window.location.origin + "/chat")}`;
});
authLangSelect.onchange = () => {
  uiLang = authLangSelect.value;
  localStorage.setItem(LANG_KEY, uiLang);
  applyTranslations();
};

authForm.onsubmit = async (e) => {
  e.preventDefault();
  authError.classList.add("hidden");
  authSubmitBtn.disabled = true;
  try {
    const endpoint = authMode === "login" ? "/api/auth/login" : "/api/auth/register";
    const body =
      authMode === "login"
        ? { email: authEmail.value.trim(), password: authPassword.value }
        : {
            email: authEmail.value.trim(),
            username: authUsername.value.trim() || authEmail.value.split("@")[0],
            password: authPassword.value,
            ui_lang: uiLang,
          };
    const data = await apiFetch(endpoint, { method: "POST", body: JSON.stringify(body) });
    authToken = data.token;
    localStorage.setItem(TOKEN_KEY, authToken);
    authPassword.value = "";
    await enterApp(data.user);
  } catch (err) {
    authError.textContent = err.message;
    authError.classList.remove("hidden");
  } finally {
    authSubmitBtn.disabled = false;
  }
};

function setLoggedOut() {
  authToken = "";
  currentUser = null;
  localStorage.removeItem(TOKEN_KEY);
  mainApp.classList.add("hidden");
  settingsModal.classList.add("hidden");
  plansModal.classList.add("hidden");
  authScreen.classList.remove("hidden");
  applyTranslations();
}

async function enterApp(userObj = null) {
  const meData = userObj ? { user: userObj } : await apiFetch("/api/auth/me");
  if (!plansCatalog) {
    const pData = meData.plans ? { plans: meData.plans } : await apiFetch("/api/plans");
    plansCatalog = pData.plans;
  }
  currentUser = meData.user;
  uiLang = currentUser.ui_lang || uiLang;
  localStorage.setItem(LANG_KEY, uiLang);

  authScreen.classList.add("hidden");
  mainApp.classList.remove("hidden");
  updateUserUI();
  applyTranslations();
  await refreshChatList();
  renderWelcome();
}

function updateUserUI() {
  if (!currentUser) return;
  sidebarUsername.textContent = currentUser.username;
  sidebarEmail.textContent = currentUser.email;
  settingsUsername.value = currentUser.username;
  userPlanBadge.textContent = currentUser.plan_name || currentUser.plan;
  userPlanBadge.className = `plan-badge plan-${currentUser.plan}`;
  updateUsageBars(currentUser.usage);
  updateRenewBanner();
}

function updateRenewBanner() {
  if (!currentUser || currentUser.plan === "free" || !currentUser.plan_expires_at) {
    renewBanner.classList.add("hidden");
    return;
  }
  const secLeft = currentUser.plan_expires_at - Math.floor(Date.now() / 1000);
  if (secLeft > 0 && secLeft <= 2 * 86400) {
    const hrs = Math.max(1, Math.ceil(secLeft / 3600));
    const fn = (I18N[uiLang] || I18N.ru).renewPrompt;
    renewText.textContent = fn(currentUser.plan_name || currentUser.plan, hrs);
    renewBanner.classList.remove("hidden");
    renewBtn.onclick = () => startCheckout(currentUser.plan, renewBtn, "monthly");
  } else {
    renewBanner.classList.add("hidden");
  }
}

function updateUsageBars(u) {
  if (!u) return;
  qMsgVal.textContent = `${u.messages_24h}/${u.messages_24h_limit} · ${u.messages_4h}/${u.messages_4h_limit}`;
  qThinkVal.textContent = `${u.think_24h}/${u.think_24h_limit}`;
  qWebVal.textContent = `${u.web_24h}/${u.web_24h_limit}`;
}

// --- Chats ---
async function refreshChatList() {
  const data = await apiFetch("/api/chats");
  chats = data.chats || [];
  renderChatList();
}

function renderChatList() {
  chatList.innerHTML = "";
  for (const c of chats) {
    const row = document.createElement("div");
    row.className = `chat-item${c.id === activeChatId ? " active" : ""}`;
    const titleSpan = document.createElement("span");
    titleSpan.className = "chat-item-title";
    titleSpan.textContent = c.title;
    const delBtn = document.createElement("button");
    delBtn.className = "chat-del";
    delBtn.type = "button";
    delBtn.textContent = "✕";
    delBtn.onclick = async (ev) => {
      ev.stopPropagation();
      if (!confirm(t("deleteConfirm"))) return;
      await apiFetch(`/api/chats/${c.id}`, { method: "DELETE" });
      if (activeChatId === c.id) {
        activeChatId = null;
        renderWelcome();
      }
      await refreshChatList();
    };
    row.onclick = () => selectChat(c.id, c.title);
    row.appendChild(titleSpan);
    row.appendChild(delBtn);
    chatList.appendChild(row);
  }
}

function renderWelcome() {
  activeChatId = null;
  currentChatTitle.textContent = "GorphinAI";
  chatLog.innerHTML = `
    <div class="welcome">
      <div class="logo logo-lg">G</div>
      <h2>${escapeHtml(t("welcomeTitle"))}</h2>
      <p>${escapeHtml(t("welcomeSub"))}</p>
    </div>`;
  renderChatList();
}

async function selectChat(chatId, title) {
  if (busy) return;
  closeMobileSidebar();
  activeChatId = chatId;
  currentChatTitle.textContent = title || "GorphinAI";
  renderChatList();
  const data = await apiFetch(`/api/chats/${chatId}`);
  chatLog.innerHTML = "";
  for (const m of data.messages || []) {
    if (m.role === "user") {
      appendUserBubble(m.content);
    } else if (m.role === "assistant") {
      appendAssistantBubble(m.content, m.thinking || "", m.sources || []);
    }
  }
  chatLog.scrollTop = chatLog.scrollHeight;
}

newChatBtn.onclick = () => {
  if (busy) return;
  closeMobileSidebar();
  renderWelcome();
  promptInput.focus();
};

// --- Message Rendering ---
function escapeHtml(s) {
  return String(s).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

function renderFormattedText(el, text) {
  const parts = String(text || "").split(/```([a-zA-Z0-9_+-]*)\n([\s\S]*?)```/g);
  if (parts.length === 1) {
    el.textContent = text;
    return;
  }
  el.innerHTML = "";
  for (let i = 0; i < parts.length; i += 3) {
    if (parts[i]) {
      const span = document.createElement("span");
      span.textContent = parts[i];
      el.appendChild(span);
    }
    if (i + 2 < parts.length) {
      const pre = document.createElement("pre");
      const code = document.createElement("code");
      code.textContent = parts[i + 2].replace(/\n$/, "");
      const copy = document.createElement("button");
      copy.className = "copy-btn";
      copy.type = "button";
      copy.textContent = "Copy";
      copy.onclick = () => {
        navigator.clipboard.writeText(code.textContent);
        copy.textContent = "Copied";
        setTimeout(() => (copy.textContent = "Copy"), 1200);
      };
      pre.appendChild(copy);
      pre.appendChild(code);
      el.appendChild(pre);
    }
  }
}

function clearWelcome() {
  const w = chatLog.querySelector(".welcome");
  if (w) w.remove();
}

function appendUserBubble(text) {
  clearWelcome();
  const div = document.createElement("div");
  div.className = "msg msg-user";
  div.textContent = text;
  chatLog.appendChild(div);
  chatLog.scrollTop = chatLog.scrollHeight;
}

function renderSourcesInto(slot, sources) {
  slot.innerHTML = "";
  if (!Array.isArray(sources) || sources.length === 0) return;
  slot.className = "sources-box";
  for (const s of sources) {
    const a = document.createElement("a");
    a.className = "source-pill";
    a.href = s.url;
    a.target = "_blank";
    a.rel = "noopener noreferrer";
    a.textContent = `🔗 ${s.title || s.url}`;
    slot.appendChild(a);
  }
}

function appendAssistantBubble(answer, thinking = "", sources = []) {
  clearWelcome();
  const wrap = document.createElement("div");
  wrap.className = "msg msg-bot";
  const sourcesSlot = document.createElement("div");
  renderSourcesInto(sourcesSlot, sources);
  wrap.appendChild(sourcesSlot);

  if (thinking) {
    const details = document.createElement("details");
    details.className = "think-box";
    const summary = document.createElement("summary");
    summary.textContent = t("thinkingDone");
    const body = document.createElement("div");
    body.className = "think-content";
    body.textContent = thinking;
    details.appendChild(summary);
    details.appendChild(body);
    wrap.appendChild(details);
  }

  const textSlot = document.createElement("div");
  renderFormattedText(textSlot, answer);
  wrap.appendChild(textSlot);
  chatLog.appendChild(wrap);
  return wrap;
}

// --- Streaming Send ---
thinkBtn.onclick = () => {
  const order = ["auto", "on", "off"];
  thinkMode = order[(order.indexOf(thinkMode) + 1) % order.length];
  syncToolPills();
};
webBtn.onclick = () => {
  webEnabled = !webEnabled;
  syncToolPills();
};

promptInput.addEventListener("input", () => {
  promptInput.style.height = "auto";
  promptInput.style.height = `${Math.min(promptInput.scrollHeight, 180)}px`;
});
promptInput.addEventListener("keydown", (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();
    chatForm.requestSubmit();
  }
});

chatForm.onsubmit = async (e) => {
  e.preventDefault();
  const text = promptInput.value.trim();
  if (!text || busy) return;

  busy = true;
  sendBtn.disabled = true;
  promptInput.value = "";
  promptInput.style.height = "auto";

  appendUserBubble(text);

  clearWelcome();
  const botBubble = document.createElement("div");
  botBubble.className = "msg msg-bot";
  const statusSlot = document.createElement("div");
  const sourcesSlot = document.createElement("div");
  const thinkDetails = document.createElement("details");
  thinkDetails.className = "think-box hidden";
  thinkDetails.open = true;
  const thinkSummary = document.createElement("summary");
  thinkSummary.textContent = t("thinkingLive");
  const thinkBody = document.createElement("div");
  thinkBody.className = "think-content";
  thinkDetails.appendChild(thinkSummary);
  thinkDetails.appendChild(thinkBody);

  const textSlot = document.createElement("div");
  textSlot.textContent = "...";

  botBubble.appendChild(statusSlot);
  botBubble.appendChild(sourcesSlot);
  botBubble.appendChild(thinkDetails);
  botBubble.appendChild(textSlot);
  chatLog.appendChild(botBubble);
  chatLog.scrollTop = chatLog.scrollHeight;

  let fullThink = "";
  let fullReply = "";
  const wasNewChat = !activeChatId;

  try {
    const res = await fetch(`${API_BASE}/api/chat/stream`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Session-Token": authToken,
      },
      body: JSON.stringify({
        chat_id: activeChatId,
        content: text,
        think_mode: thinkMode,
        web_search: webEnabled,
      }),
    });

    if (!res.ok) {
      const errJson = await res.json().catch(() => ({}));
      throw new Error(errJson.detail || `HTTP ${res.status}`);
    }

    const reader = res.body.getReader();
    const decoder = new TextDecoder();
    let buffer = "";

    while (true) {
      const { value, done } = await reader.read();
      if (done) break;
      buffer += decoder.decode(value, { stream: true });
      const lines = buffer.split("\n");
      buffer = lines.pop() || "";

      for (const line of lines) {
        if (!line.startsWith("data: ")) continue;
        const dataStr = line.slice(6).trim();
        if (dataStr === "[DONE]") continue;
        const evt = JSON.parse(dataStr);
        if (evt.error) throw new Error(evt.error);

        if (evt.status === "chat_meta") {
          activeChatId = evt.chat_id;
          if (wasNewChat) currentChatTitle.textContent = text.slice(0, 55);
          continue;
        }
        if (evt.status === "searching") {
          statusSlot.className = "search-status";
          statusSlot.textContent = t("searching");
          textSlot.textContent = "";
          continue;
        }
        if (evt.status === "sources") {
          statusSlot.remove();
          renderSourcesInto(sourcesSlot, evt.sources || []);
          textSlot.textContent = "...";
          continue;
        }
        if (evt.status === "thinking") {
          thinkDetails.classList.remove("hidden");
          textSlot.textContent = "";
          continue;
        }
        if (evt.think) {
          thinkDetails.classList.remove("hidden");
          fullThink += evt.think;
          thinkBody.textContent = fullThink;
          chatLog.scrollTop = chatLog.scrollHeight;
          continue;
        }
        if (evt.token) {
          if (fullThink && thinkDetails.open) {
            thinkDetails.open = false;
            thinkSummary.textContent = t("thinkingDone");
          }
          fullReply += evt.token;
          textSlot.textContent = fullReply;
          chatLog.scrollTop = chatLog.scrollHeight;
          continue;
        }
        if (evt.status === "usage" && evt.usage) {
          if (currentUser) currentUser.usage = evt.usage;
          updateUsageBars(evt.usage);
        }
      }
    }

    if (statusSlot.parentNode) statusSlot.remove();
    if (!fullThink) thinkDetails.remove();
    else {
      thinkDetails.open = false;
      thinkSummary.textContent = t("thinkingDone");
    }
    renderFormattedText(textSlot, fullReply.trim());
    await refreshChatList();
  } catch (err) {
    botBubble.className = "msg msg-err";
    botBubble.textContent = err.message;
  } finally {
    busy = false;
    sendBtn.disabled = false;
    promptInput.focus();
  }
};

// --- Settings & Mobile Sidebar ---
function openModal(el) { el.classList.remove("hidden"); }
function closeModal(el) { el.classList.add("hidden"); }

document.querySelectorAll(".close-modal").forEach((btn) => {
  btn.onclick = () => closeModal(document.getElementById(btn.getAttribute("data-close")));
});

document.getElementById("openSettingsBtn").onclick = () => {
  settingsSavedMsg.classList.add("hidden");
  openModal(settingsModal);
};
document.getElementById("topSettingsBtn").onclick = () => {
  settingsSavedMsg.classList.add("hidden");
  openModal(settingsModal);
};

saveSettingsBtn.onclick = async () => {
  saveSettingsBtn.disabled = true;
  try {
    const newLang = settingsLangSelect.value;
    const newName = settingsUsername.value.trim();
    const data = await apiFetch("/api/settings", {
      method: "PATCH",
      body: JSON.stringify({ ui_lang: newLang, username: newName }),
    });
    currentUser = data.user;
    uiLang = currentUser.ui_lang;
    localStorage.setItem(LANG_KEY, uiLang);
    updateUserUI();
    applyTranslations();
    settingsSavedMsg.classList.remove("hidden");
  } finally {
    saveSettingsBtn.disabled = false;
  }
};

logoutBtn.onclick = async () => {
  await apiFetch("/api/auth/logout", { method: "POST" }).catch(() => {});
  setLoggedOut();
};

mobileMenuBtn.onclick = () => {
  sidebar.classList.add("open");
  sidebarBackdrop.classList.remove("hidden");
};
function closeMobileSidebar() {
  sidebar.classList.remove("open");
  sidebarBackdrop.classList.add("hidden");
}
sidebarBackdrop.onclick = closeMobileSidebar;

// --- Tariffs & PayGate.to Checkout ---
document.getElementById("openPlansBtn").onclick = () => {
  plansError.classList.add("hidden");
  renderPlans();
  openModal(plansModal);
};
document.getElementById("topPlansBtn").onclick = () => {
  plansError.classList.add("hidden");
  renderPlans();
  openModal(plansModal);
};

billMonthly.onclick = () => {
  billingPeriod = "monthly";
  billMonthly.classList.add("active");
  billYearly.classList.remove("active");
  renderPlans();
};
billYearly.onclick = () => {
  billingPeriod = "yearly";
  billYearly.classList.add("active");
  billMonthly.classList.remove("active");
  renderPlans();
};

function renderPlans() {
  if (!plansCatalog) return;
  plansGrid.innerHTML = "";
  const roles = (I18N[uiLang] || I18N.ru).planRoles;
  const feat = (I18N[uiLang] || I18N.ru).planFeat;

  for (const key of ["free", "basic", "plus", "pro"]) {
    const p = plansCatalog[key];
    if (!p) continue;
    const card = document.createElement("div");
    card.className = `plan-card${key === "plus" ? " popular" : ""}`;
    const price = billingPeriod === "yearly" ? p.price_yearly_usd : p.price_monthly_usd;
    const suffix = key === "free" ? "" : billingPeriod === "yearly" ? t("perYr") : t("perMo");
    const isCurrent = currentUser && currentUser.plan === key;

    const ul = document.createElement("ul");
    ul.className = "plan-features";
    const items = [
      feat.msg(p.messages_per_day, p.messages_per_4h),
      feat.think(p.think_per_day, p.max_think_tokens),
      feat.web(p.web_per_day, p.max_web_urls),
    ];
    if (p.direct_qa) items.push(feat.proExtra);
    for (const itemText of items) {
      const li = document.createElement("li");
      li.textContent = itemText;
      ul.appendChild(li);
    }

    const actionBtn = document.createElement("button");
    actionBtn.type = "button";
    if (key === "free") {
      actionBtn.className = "btn btn-full";
      actionBtn.disabled = true;
      actionBtn.textContent = isCurrent ? t("currentPlan") : "Free";
    } else if (isCurrent) {
      actionBtn.className = "btn btn-outline btn-full";
      actionBtn.textContent = t("renewBtn");
      actionBtn.onclick = () => startCheckout(key, actionBtn);
    } else {
      actionBtn.className = "btn btn-primary btn-full";
      actionBtn.textContent = t("choosePlan");
      actionBtn.onclick = () => startCheckout(key, actionBtn);
    }

    card.innerHTML = `
      <div class="plan-name">${escapeHtml(p.name)}</div>
      <div class="plan-price">$${price}${suffix ? ` <span>${escapeHtml(suffix)}</span>` : ""}</div>
      <div class="plan-role">${escapeHtml(roles[key] || "")}</div>
    `;
    card.appendChild(ul);
    card.appendChild(actionBtn);
    plansGrid.appendChild(card);
  }
}

async function startCheckout(planKey, btn, overridePeriod = null) {
  plansError.classList.add("hidden");
  btn.disabled = true;
  try {
    const data = await apiFetch("/api/billing/checkout", {
      method: "POST",
      body: JSON.stringify({ plan: planKey, billing_period: overridePeriod || billingPeriod }),
    });
    window.open(data.checkout_url, "_blank", "noopener,noreferrer");
  } catch (err) {
    plansError.textContent = err.message;
    plansError.classList.remove("hidden");
  } finally {
    btn.disabled = false;
  }
}

// --- Init ---
(async function init() {
  applyTranslations();
  if (!authToken) {
    setLoggedOut();
    return;
  }
  try {
    await enterApp();
  } catch {
    setLoggedOut();
  }
})();
