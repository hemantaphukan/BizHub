/**
 * BizHub Enterprise Commerce & Services Web Application
 * Handles both Front-End (Client Portal) and Backend (Admin Dashboard)
 * Supports offline-first LocalStorage with optional Firebase Cloud Firestore integration.
 */

// ================= INITIAL DATA SEED =================
const DEFAULT_PRODUCTS = [
  {
    id: "PROD-101",
    name: "Apex Industrial Servo Motor X4",
    category: "Automation",
    sku: "IND-SRV-04",
    description: "High-torque precision brushless servo motor with integrated 18-bit absolute encoder.",
    price: 450.00,
    stock: 18,
    minAlert: 5,
    unit: "units"
  },
  {
    id: "PROD-102",
    name: "OptiCore Gigabit Fiber Transceiver",
    category: "IT & Cloud",
    sku: "NET-FBR-10G",
    description: "10G SFP+ single-mode optical transceiver module with 10km transmission range.",
    price: 120.00,
    stock: 4,
    minAlert: 6,
    unit: "pcs"
  },
  {
    id: "PROD-103",
    name: "Titanium CNC End Mill Tooling Kit",
    category: "Tooling",
    sku: "MCH-MILL-TI",
    description: "4-flute carbide end mills with nano-composite coating for aerospace alloy machining.",
    price: 280.00,
    stock: 25,
    minAlert: 8,
    unit: "kits"
  },
  {
    id: "PROD-104",
    name: "Thermal Dissipation Heat Sink Array",
    category: "Hardware",
    sku: "THM-SNK-200",
    description: "Anodized aluminum vapor-chamber heat sink designed for heavy power inverter stacks.",
    price: 85.50,
    stock: 0,
    minAlert: 10,
    unit: "pcs"
  },
  {
    id: "PROD-105",
    name: "SecureCore Embedded IoT Gateway",
    category: "IT & Cloud",
    sku: "IOT-GTW-X2",
    description: "DIN-rail industrial edge computing gateway with dual LTE failover and TPM 2.0 security.",
    price: 620.00,
    stock: 12,
    minAlert: 4,
    unit: "units"
  },
  {
    id: "PROD-106",
    name: "UltraPure Electrostatic Filter Pack",
    category: "Consumables",
    sku: "FLT-IND-01",
    description: "HEPA H14 certified industrial air purification cartridge for cleanroom environments.",
    price: 65.00,
    stock: 40,
    minAlert: 15,
    unit: "packs"
  }
];

const DEFAULT_ORDERS = [
  {
    id: "ORD-8921",
    customerName: "Alex Chen",
    customerEmail: "alex.chen@nexuscorp.com",
    items: [
      { productId: "PROD-101", name: "Apex Industrial Servo Motor X4", qty: 2, price: 450.00 },
      { productId: "PROD-103", name: "Titanium CNC End Mill Tooling Kit", qty: 1, price: 280.00 }
    ],
    totalAmount: 1180.00,
    status: "DELIVERED",
    statusIndex: 4,
    placedAt: Date.now() - 86400000 * 2,
    shippingAddress: "Building 4, Dock B, 750 Enterprise Blvd, CA",
    notes: "Please include official calibration certificate.",
    attachments: [
      {
        id: "ATT-991",
        fileName: "Official_Commercial_Invoice_ORD8921.pdf",
        fileType: "PDF",
        sizeKb: 145,
        uploadedAt: Date.now() - 86400000 * 2,
        desc: "Official Commercial Tax Invoice & Fulfillment Slip"
      },
      {
        id: "ATT-992",
        fileName: "Dock_Delivery_Inspection_Proof_ORD8921.jpg",
        fileType: "JPG",
        sizeKb: 420,
        uploadedAt: Date.now() - 86400000 * 1,
        desc: "Certified Dock B Delivery Photo & Stamp"
      }
    ]
  },
  {
    id: "ORD-9104",
    customerName: "Alex Chen",
    customerEmail: "alex.chen@nexuscorp.com",
    items: [
      { productId: "PROD-105", name: "SecureCore Embedded IoT Gateway", qty: 1, price: 620.00 }
    ],
    totalAmount: 620.00,
    status: "IN_PROGRESS",
    statusIndex: 2,
    placedAt: Date.now() - 3600000 * 10,
    shippingAddress: "Building 4, Dock B, 750 Enterprise Blvd, CA",
    notes: "Firmware v3.4 pre-configuration requested.",
    attachments: [
      {
        id: "ATT-993",
        fileName: "Config_Work_Order_ORD9104.pdf",
        fileType: "PDF",
        sizeKb: 128,
        uploadedAt: Date.now() - 3600000 * 6,
        desc: "Engineering Assembly & Configuration Spec"
      }
    ]
  }
];

const DEFAULT_SERVICES = [
  {
    id: "SRV-5021",
    customerName: "Alex Chen",
    customerEmail: "alex.chen@nexuscorp.com",
    serviceType: "Custom Precision CNC Fabrication",
    requirements: "Fabrication of 4 custom anodized mounting brackets as per technical drawing rev B. Tolerance +/- 0.02mm, 6061-T6 aluminum.",
    urgency: "High",
    budget: 1450.00,
    status: "COMPLETED",
    statusIndex: 4,
    submittedAt: Date.now() - 86400000 * 3,
    adminNotes: "Dimensional tolerances laser-verified. Anodizing pass rate 100%.",
    attachments: [
      {
        id: "ATT-881",
        fileName: "CNC_Quality_Verification_Report_SRV5021.pdf",
        fileType: "PDF",
        sizeKb: 210,
        uploadedAt: Date.now() - 86400000 * 2,
        desc: "CMM Coordinate Laser Measurement Inspection"
      },
      {
        id: "ATT-882",
        fileName: "Engineering_Compliance_Seal_SRV5021.jpg",
        fileType: "JPG",
        sizeKb: 380,
        uploadedAt: Date.now() - 86400000 * 1,
        desc: "ISO Certified Material & Tolerance Badge"
      }
    ]
  },
  {
    id: "SRV-5490",
    customerName: "Alex Chen",
    customerEmail: "alex.chen@nexuscorp.com",
    serviceType: "Enterprise IT Architecture & Security Audit",
    requirements: "Security vulnerability audit and performance stress test for our distributed IoT edge broker cluster.",
    urgency: "Normal",
    budget: 2200.00,
    status: "APPROVED",
    statusIndex: 2,
    submittedAt: Date.now() - 3600000 * 18,
    adminNotes: "Scheduled for remote testing window this Thursday.",
    attachments: [
      {
        id: "ATT-883",
        fileName: "Security_Audit_Scope_Agreement_SRV5490.pdf",
        fileType: "PDF",
        sizeKb: 175,
        uploadedAt: Date.now() - 3600000 * 4,
        desc: "Authorized Pen-Test & Vulnerability Scope Document"
      }
    ]
  }
];

const DEFAULT_PROFILE = {
  name: "Alex Chen",
  email: "alex.chen@nexuscorp.com",
  phone: "+1 (555) 234-8901",
  company: "Nexus Tech Industries Ltd.",
  shippingAddress: "Suite 400, 750 Enterprise Blvd, Tech District, CA 94105",
  tier: "Gold Enterprise Client"
};

// ================= STATE MANAGEMENT =================
class AppState {
  constructor() {
    this.currentRole = "CUSTOMER"; // "CUSTOMER" or "ADMIN"
    this.activeTab = "catalog";
    this.products = this.load("biz_products", DEFAULT_PRODUCTS);
    this.orders = this.load("biz_orders", DEFAULT_ORDERS);
    this.services = this.load("biz_services", DEFAULT_SERVICES);
    this.profile = this.load("biz_profile", DEFAULT_PROFILE);
    this.cart = {}; // { productId: qty }
    this.firebaseConfig = this.load("biz_firebase_config", null);
  }

  save(key, data) {
    try {
      localStorage.setItem(key, JSON.stringify(data));
    } catch (e) {
      console.warn("Storage full or unavailable", e);
    }
  }

  load(key, fallback) {
    try {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : fallback;
    } catch {
      return fallback;
    }
  }

  persistAll() {
    this.save("biz_products", this.products);
    this.save("biz_orders", this.orders);
    this.save("biz_services", this.services);
    this.save("biz_profile", this.profile);
  }
}

const state = new AppState();

// ================= DOCUMENT GENERATOR (PDF & JPG PREVIEW) =================
const DocumentEngine = {
  renderDocumentToCanvas(attachment) {
    const canvas = document.createElement("canvas");
    canvas.className = "rendered-doc-page";
    const ctx = canvas.getContext("2d");

    if (attachment.fileType === "PDF") {
      canvas.width = 750;
      canvas.height = 1000;

      // Header Banner
      ctx.fillStyle = "#1E3A8A";
      ctx.fillRect(0, 0, 750, 130);

      ctx.fillStyle = "#FFFFFF";
      ctx.font = "bold 26px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("BIZHUB ENTERPRISE SYSTEMS", 40, 55);

      ctx.font = "14px 'Plus Jakarta Sans', sans-serif";
      ctx.fillStyle = "#93C5FD";
      ctx.fillText("Official Commercial Document & Audit Record", 40, 85);
      ctx.fillText(`Document ID: ${attachment.id} • ${new Date(attachment.uploadedAt).toLocaleDateString()}`, 40, 110);

      // Meta Block
      ctx.fillStyle = "#0F172A";
      ctx.font = "bold 18px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText(attachment.fileName, 40, 190);

      ctx.fillStyle = "#475569";
      ctx.font = "14px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText(`Description: ${attachment.desc}`, 40, 220);
      ctx.fillText(`Issued For: ${state.profile.name} (${state.profile.company})`, 40, 245);
      ctx.fillText(`Destination: ${state.profile.shippingAddress}`, 40, 270);

      // Content Box
      ctx.fillStyle = "#F8FAFC";
      ctx.fillRect(40, 310, 670, 380);
      ctx.strokeStyle = "#E2E8F0";
      ctx.lineWidth = 1;
      ctx.strokeRect(40, 310, 670, 380);

      ctx.fillStyle = "#0D9488";
      ctx.font = "bold 15px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("SPECIFICATION & FULFILLMENT VERIFICATION", 60, 350);

      ctx.fillStyle = "#1E293B";
      ctx.font = "13px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("• All items inspected in compliance with ISO Quality Assurance standards.", 60, 390);
      ctx.fillText("• Packaging seals intact, ESD anti-static containers verified.", 60, 420);
      ctx.fillText("• Digital authorization key matches corporate logistics manifest.", 60, 450);
      ctx.fillText("• Authorized Admin Signature Stamp: VERIFIED & CLEARED", 60, 480);

      // Stamp
      ctx.strokeStyle = "#047857";
      ctx.lineWidth = 3;
      ctx.strokeRect(60, 520, 320, 80);
      ctx.fillStyle = "#047857";
      ctx.font = "bold 16px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("OFFICIALLY APPROVED", 80, 555);
      ctx.font = "11px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("BizHub Central Administration Console", 80, 580);

      // Footer
      ctx.fillStyle = "#94A3B8";
      ctx.font = "11px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("Confidential document generated exclusively for designated client account.", 40, 960);

    } else {
      // High-res JPG Deliverable Proof
      canvas.width = 800;
      canvas.height = 800;

      ctx.fillStyle = "#0F172A";
      ctx.fillRect(0, 0, 800, 800);

      // Border
      ctx.strokeStyle = "#14B8A6";
      ctx.lineWidth = 10;
      ctx.strokeRect(30, 30, 740, 740);

      ctx.fillStyle = "#1E3A8A";
      ctx.fillRect(60, 60, 680, 140);

      ctx.fillStyle = "#FFFFFF";
      ctx.font = "bold 28px 'Plus Jakarta Sans', sans-serif";
      ctx.textAlign = "center";
      ctx.fillText("BIZHUB ENTERPRISE QUALITY CERTIFICATION", 400, 120);

      ctx.fillStyle = "#38BDF8";
      ctx.font = "16px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("Official Inspection & Delivery Proof Badge", 400, 160);

      // Center Badge
      ctx.fillStyle = "#0D9488";
      ctx.beginPath();
      ctx.arc(400, 400, 100, 0, Math.PI * 2);
      ctx.fill();

      ctx.strokeStyle = "#5EEAD4";
      ctx.lineWidth = 6;
      ctx.stroke();

      ctx.fillStyle = "#FFFFFF";
      ctx.font = "bold 64px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText("✓", 400, 422);

      // Subtext
      ctx.font = "bold 22px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText(attachment.fileName, 400, 560);

      ctx.fillStyle = "#94A3B8";
      ctx.font = "15px 'Plus Jakarta Sans', sans-serif";
      ctx.fillText(`Client: ${state.profile.name} • ${attachment.desc}`, 400, 600);
      ctx.fillText(`Generated: ${new Date(attachment.uploadedAt).toLocaleString()}`, 400, 630);
      ctx.fillText("HASH: BZ-" + Math.random().toString(36).substring(2, 12).toUpperCase(), 400, 710);
      ctx.textAlign = "left";
    }

    return canvas;
  },

  download(attachment) {
    const canvas = this.renderDocumentToCanvas(attachment);
    const link = document.createElement("a");
    link.download = attachment.fileName;
    link.href = canvas.toDataURL(attachment.fileType === "PDF" ? "image/png" : "image/jpeg");
    link.click();
    showToast(`Downloading ${attachment.fileName}...`);
  }
};

// ================= DOM ELEMENT REFERENCES =================
const roleBadge = document.getElementById("roleBadge");
const roleToggleBtn = document.getElementById("roleToggleBtn");
const roleToggleLabel = document.getElementById("roleToggleLabel");
const clientNav = document.getElementById("clientNav");
const openCartBtn = document.getElementById("openCartBtn");
const cartCountBadge = document.getElementById("cartCountBadge");
const toastContainer = document.getElementById("toastContainer");

// Modals
const cartDrawerOverlay = document.getElementById("cartDrawerOverlay");
const serviceModalOverlay = document.getElementById("serviceModalOverlay");
const productModalOverlay = document.getElementById("productModalOverlay");
const uploadFileModalOverlay = document.getElementById("uploadFileModalOverlay");
const docViewerModalOverlay = document.getElementById("docViewerModalOverlay");
const firebaseModalOverlay = document.getElementById("firebaseModalOverlay");

// Targets
let currentUploadTarget = null; // { type: 'order'|'service', item: object }
let activeDocAttachment = null;

// ================= CORE UI CONTROLLERS =================

function showToast(msg) {
  const toast = document.createElement("div");
  toast.className = "toast";
  toast.innerHTML = `<span>✓</span> <span>${msg}</span>`;
  toastContainer.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = "0";
    setTimeout(() => toast.remove(), 300);
  }, 2800);
}

// Switch Role between Customer and Admin
function switchRole(role) {
  state.currentRole = role;
  if (role === "ADMIN") {
    roleBadge.textContent = "BACKEND ADMIN";
    roleBadge.classList.add("admin-mode");
    roleToggleBtn.classList.add("admin-mode");
    roleToggleLabel.textContent = "Switch to Client";
    clientNav.classList.add("hidden");
    openCartBtn.classList.add("hidden");
    activateTab("admin");
  } else {
    roleBadge.textContent = "CLIENT PORTAL";
    roleBadge.classList.remove("admin-mode");
    roleToggleBtn.classList.remove("admin-mode");
    roleToggleLabel.textContent = "Switch to Admin";
    clientNav.classList.remove("hidden");
    openCartBtn.classList.remove("hidden");
    activateTab("catalog");
  }
}

// Switch Main Tab
function activateTab(tabId) {
  state.activeTab = tabId;
  document.querySelectorAll(".tab-pane").forEach(pane => pane.classList.remove("active"));
  document.querySelectorAll(".nav-btn").forEach(btn => {
    btn.classList.toggle("active", btn.dataset.tab === tabId);
  });

  const targetPane = document.getElementById(`${tabId}Tab`);
  if (targetPane) targetPane.classList.add("active");

  // Re-render data for current view
  if (tabId === "catalog") renderCatalog();
  if (tabId === "tracking") renderTracking();
  if (tabId === "profile") renderProfile();
  if (tabId === "admin") renderAdmin();
}

// ================= TAB 1: CATALOG RENDERING =================
function renderCatalog() {
  const grid = document.getElementById("productsGrid");
  const chipsContainer = document.getElementById("categoryChips");
  const searchInput = document.getElementById("catalogSearchInput").value.toLowerCase();

  // Categories
  const categories = ["All", ...new Set(state.products.map(p => p.category))];
  const activeCategory = chipsContainer.querySelector(".chip.active")?.dataset.cat || "All";

  chipsContainer.innerHTML = categories.map(cat => `
    <button class="chip ${cat === activeCategory ? 'active' : ''}" data-cat="${cat}">${cat}</button>
  `).join("");

  chipsContainer.querySelectorAll(".chip").forEach(chip => {
    chip.onclick = () => {
      chipsContainer.querySelectorAll(".chip").forEach(c => c.classList.remove("active"));
      chip.classList.add("active");
      renderCatalog();
    };
  });

  const filtered = state.products.filter(p => {
    const matchCat = activeCategory === "All" || p.category === activeCategory;
    const matchSearch = p.name.toLowerCase().includes(searchInput) || p.sku.toLowerCase().includes(searchInput);
    return matchCat && matchSearch;
  });

  if (filtered.length === 0) {
    grid.innerHTML = `<p class="section-desc">No products match your criteria.</p>`;
    return;
  }

  grid.innerHTML = filtered.map(prod => {
    const isOut = prod.stock <= 0;
    const isLow = prod.stock > 0 && prod.stock <= prod.minAlert;
    const stockClass = isOut ? "stock-out" : (isLow ? "stock-low" : "stock-in");
    const stockLabel = isOut ? "Out of Stock" : (isLow ? `Only ${prod.stock} left` : `${prod.stock} in stock`);

    return `
      <div class="product-card">
        <div>
          <div class="prod-meta-top">
            <span class="prod-cat">${prod.category}</span>
            <span class="prod-sku">SKU: ${prod.sku}</span>
          </div>
          <h4 class="prod-name">${prod.name}</h4>
          <p class="prod-desc">${prod.description}</p>
        </div>
        <div class="prod-footer">
          <div>
            <div class="prod-price">$${prod.price.toFixed(2)}</div>
            <div class="stock-tag ${stockClass}">● ${stockLabel}</div>
          </div>
          <button class="btn btn-primary btn-sm add-to-cart-btn" data-id="${prod.id}" ${isOut ? 'disabled' : ''}>
            ${isOut ? 'Unavailable' : '+ Add to Order'}
          </button>
        </div>
      </div>
    `;
  }).join("");

  grid.querySelectorAll(".add-to-cart-btn").forEach(btn => {
    btn.onclick = () => {
      addToCart(btn.dataset.id);
    };
  });
}

// Cart operations
function addToCart(prodId) {
  state.cart[prodId] = (state.cart[prodId] || 0) + 1;
  updateCartBadge();
  showToast("Item added to order cart");
}

function updateCartBadge() {
  const totalItems = Object.values(state.cart).reduce((a, b) => a + b, 0);
  cartCountBadge.textContent = totalItems;
}

function openCartDrawer() {
  const list = document.getElementById("cartItemsList");
  const addressInput = document.getElementById("cartShippingAddressInput");
  addressInput.value = state.profile.shippingAddress;

  const entries = Object.entries(state.cart);
  if (entries.length === 0) {
    list.innerHTML = `<p class="text-muted" style="text-align: center; padding: 2rem 0;">Your cart is currently empty.</p>`;
    document.getElementById("cartTotalDisplay").textContent = "$0.00";
  } else {
    let total = 0;
    list.innerHTML = entries.map(([id, qty]) => {
      const prod = state.products.find(p => p.id === id);
      if (!prod) return "";
      const sub = prod.price * qty;
      total += sub;
      return `
        <div class="cart-item-row">
          <div>
            <div style="font-weight:700; font-size:0.9rem;">${prod.name}</div>
            <div style="font-size:0.75rem; color:#64748B;">$${prod.price.toFixed(2)} each</div>
          </div>
          <div class="cart-qty-ctrl">
            <button class="qty-btn" onclick="window.updateCartQty('${id}', ${qty - 1})">-</button>
            <span style="font-weight:700; font-size:0.9rem;">${qty}</span>
            <button class="qty-btn" onclick="window.updateCartQty('${id}', ${qty + 1})">+</button>
          </div>
        </div>
      `;
    }).join("");
    document.getElementById("cartTotalDisplay").textContent = `$${total.toFixed(2)}`;
  }

  cartDrawerOverlay.classList.add("open");
}

window.updateCartQty = function(id, newQty) {
  if (newQty <= 0) {
    delete state.cart[id];
  } else {
    state.cart[id] = newQty;
  }
  updateCartBadge();
  openCartDrawer();
};

function submitOrder() {
  const entries = Object.entries(state.cart);
  if (entries.length === 0) return alert("Please add products to cart first.");

  const address = document.getElementById("cartShippingAddressInput").value || state.profile.shippingAddress;
  const notes = document.getElementById("cartNotesInput").value;

  const items = entries.map(([id, qty]) => {
    const prod = state.products.find(p => p.id === id);
    return { productId: id, name: prod.name, qty, price: prod.price };
  });

  const totalAmount = items.reduce((acc, item) => acc + (item.price * item.qty), 0);
  const newOrder = {
    id: "ORD-" + Math.floor(1000 + Math.random() * 9000),
    customerName: state.profile.name,
    customerEmail: state.profile.email,
    items,
    totalAmount,
    status: "PENDING",
    statusIndex: 0,
    placedAt: Date.now(),
    shippingAddress: address,
    notes,
    attachments: []
  };

  // Decrement stock
  entries.forEach(([id, qty]) => {
    const prod = state.products.find(p => p.id === id);
    if (prod) prod.stock = Math.max(0, prod.stock - qty);
  });

  state.orders.unshift(newOrder);
  state.cart = {};
  state.persistAll();
  updateCartBadge();
  cartDrawerOverlay.classList.remove("open");
  showToast(`Order ${newOrder.id} submitted successfully!`);
  activateTab("tracking");
}

// ================= TAB 2: SERVICES RENDERING =================
const SERVICE_OFFERINGS = [
  {
    title: "Custom Precision CNC Fabrication",
    desc: "Subtractive milling & turning for aerospace aluminum, titanium, and stainless steel tolerances +/- 0.02mm.",
    turnaround: "3-5 Business Days",
    starting: "From $500"
  },
  {
    title: "Enterprise IT Architecture & Security Audit",
    desc: "Vulnerability analysis, distributed broker stress testing, and cloud infrastructure latency optimization.",
    turnaround: "24-48 Hours",
    starting: "From $1,200"
  },
  {
    title: "Precision Calibration & Stress Testing",
    desc: "Thermal, vibrational, and electrical tolerance testing with signed digital QA certificates.",
    turnaround: "1-2 Business Days",
    starting: "From $350"
  },
  {
    title: "Industrial Equipment On-Site Maintenance",
    desc: "Servo motor overhaul, actuator rebuilds, and preventative diagnostics by certified field engineers.",
    turnaround: "Scheduled Dispatch",
    starting: "From $400 / session"
  }
];

function renderServices() {
  const grid = document.getElementById("servicesGrid");
  grid.innerHTML = SERVICE_OFFERINGS.map(srv => `
    <div class="service-card">
      <div>
        <h4>${srv.title}</h4>
        <p>${srv.desc}</p>
      </div>
      <div class="service-meta-row">
        <div>
          <span style="font-size:0.75rem; color:#64748B;">Turnaround: </span>
          <strong style="font-size:0.8rem;">${srv.turnaround}</strong>
        </div>
        <button class="btn btn-outline btn-sm" onclick="window.presetServiceRequest('${srv.title}')">
          Request Service
        </button>
      </div>
    </div>
  `).join("");
}

window.presetServiceRequest = function(title) {
  document.getElementById("srvFormTitle").value = title;
  document.getElementById("srvFormRequirements").value = "";
  serviceModalOverlay.classList.add("open");
};

function submitServiceRequest(e) {
  e.preventDefault();
  const title = document.getElementById("srvFormTitle").value;
  const reqs = document.getElementById("srvFormRequirements").value;
  const urgency = document.getElementById("srvFormUrgency").value;
  const budget = parseFloat(document.getElementById("srvFormBudget").value) || 500;

  const newService = {
    id: "SRV-" + Math.floor(1000 + Math.random() * 9000),
    customerName: state.profile.name,
    customerEmail: state.profile.email,
    serviceType: title,
    requirements: reqs,
    urgency,
    budget,
    status: "SUBMITTED",
    statusIndex: 0,
    submittedAt: Date.now(),
    adminNotes: "New service project received for engineering review.",
    attachments: []
  };

  state.services.unshift(newService);
  state.persistAll();
  serviceModalOverlay.classList.remove("open");
  showToast(`Service Request ${newService.id} submitted!`);
  activateTab("tracking");
}

// ================= TAB 3: TRACKING RENDERING =================
function renderTracking() {
  const list = document.getElementById("trackingList");
  const filter = document.querySelector(".filter-pills .pill-btn.active")?.dataset.filter || "all";

  let html = "";

  if (filter === "all" || filter === "orders") {
    state.orders.forEach(order => {
      html += renderOrderTrackingCard(order);
    });
  }

  if (filter === "all" || filter === "services") {
    state.services.forEach(srv => {
      html += renderServiceTrackingCard(srv);
    });
  }

  list.innerHTML = html || `<p class="section-desc">No tracking requests found.</p>`;
}

function renderOrderTrackingCard(order) {
  const steps = [
    { name: "Placed", idx: 0 },
    { name: "Approved", idx: 1 },
    { name: "Production", idx: 2 },
    { name: "Ready", idx: 3 },
    { name: "Delivered", idx: 4 }
  ];

  return `
    <div class="tracking-card">
      <div class="tracking-card-header">
        <div>
          <span class="entity-id">${order.id}</span>
          <span style="font-size:0.75rem; color:#64748B; margin-left:0.5rem;">${new Date(order.placedAt).toLocaleString()}</span>
        </div>
        <span class="status-badge badge-${order.status.toLowerCase()}">${order.status}</span>
      </div>

      <!-- 5-Step Stepper -->
      <div class="stepper-timeline">
        ${steps.map(s => {
          const isDone = order.statusIndex >= s.idx;
          const isActive = order.statusIndex === s.idx;
          return `
            <div class="step-node ${isDone ? 'completed' : ''} ${isActive ? 'active' : ''}">
              <div class="step-circle">${isDone && !isActive ? '✓' : s.idx + 1}</div>
              <span class="step-label">${s.name}</span>
            </div>
          `;
        }).join("")}
      </div>

      <div style="font-size:0.85rem; margin-bottom:0.75rem;">
        <strong>Items:</strong> ${order.items.map(i => `${i.name} (x${i.qty})`).join(", ")}
        <div style="font-weight:700; color:var(--primary); margin-top:0.25rem;">Total Value: $${order.totalAmount.toFixed(2)}</div>
      </div>

      <!-- Uploaded Deliverable Files Section -->
      ${order.attachments && order.attachments.length > 0 ? `
        <div class="attached-files-container">
          <div class="attached-files-title">Official Deliverables Attached by Admin (${order.attachments.length}):</div>
          ${order.attachments.map(att => renderAttachmentRow(att)).join("")}
        </div>
      ` : ''}
    </div>
  `;
}

function renderServiceTrackingCard(srv) {
  const steps = [
    { name: "Submitted", idx: 0 },
    { name: "Review", idx: 1 },
    { name: "Approved", idx: 2 },
    { name: "Execution", idx: 3 },
    { name: "Completed", idx: 4 }
  ];

  return `
    <div class="tracking-card">
      <div class="tracking-card-header">
        <div>
          <span class="entity-id" style="color:var(--secondary);">${srv.id}</span>
          <strong style="margin-left:0.5rem; font-size:0.95rem;">${srv.serviceType}</strong>
        </div>
        <span class="status-badge badge-${srv.status.toLowerCase()}">${srv.status}</span>
      </div>

      <div class="stepper-timeline">
        ${steps.map(s => {
          const isDone = srv.statusIndex >= s.idx;
          const isActive = srv.statusIndex === s.idx;
          return `
            <div class="step-node ${isDone ? 'completed' : ''} ${isActive ? 'active' : ''}">
              <div class="step-circle">${isDone && !isActive ? '✓' : s.idx + 1}</div>
              <span class="step-label">${s.name}</span>
            </div>
          `;
        }).join("")}
      </div>

      <div style="font-size:0.85rem; color:#334155; margin-bottom:0.5rem;">
        <strong>Requirements:</strong> ${srv.requirements}
      </div>

      ${srv.adminNotes ? `
        <div style="background:#EFF6FF; border-radius:6px; padding:0.5rem 0.75rem; font-size:0.8rem; color:#1E3A8A; margin-bottom:0.75rem;">
          <strong>Admin Engineering Note:</strong> ${srv.adminNotes}
        </div>
      ` : ''}

      ${srv.attachments && srv.attachments.length > 0 ? `
        <div class="attached-files-container">
          <div class="attached-files-title">Service Reports & Certifications (${srv.attachments.length}):</div>
          ${srv.attachments.map(att => renderAttachmentRow(att)).join("")}
        </div>
      ` : ''}
    </div>
  `;
}

function renderAttachmentRow(att) {
  return `
    <div class="file-row">
      <div class="file-info">
        <div class="file-type-icon ${att.fileType === 'PDF' ? 'file-pdf' : 'file-jpg'}">${att.fileType}</div>
        <div>
          <div class="file-name">${att.fileName}</div>
          <div class="file-meta">${att.fileType} • ${att.sizeKb} KB • ${att.desc}</div>
        </div>
      </div>
      <div style="display: flex; gap: 0.5rem;">
        <button class="btn btn-outline btn-sm" onclick='window.previewDoc(${JSON.stringify(att)})'>View</button>
        <button class="btn btn-primary btn-sm" onclick='window.downloadDoc(${JSON.stringify(att)})'>Download</button>
      </div>
    </div>
  `;
}

// Global viewer helpers
window.previewDoc = function(attachment) {
  activeDocAttachment = attachment;
  document.getElementById("docViewerTitle").textContent = attachment.fileName;
  document.getElementById("docViewerMeta").textContent = `${attachment.fileType} • ${attachment.sizeKb} KB • ${attachment.desc}`;

  const viewerBody = document.getElementById("docViewerBody");
  viewerBody.innerHTML = "";
  const canvas = DocumentEngine.renderDocumentToCanvas(attachment);
  viewerBody.appendChild(canvas);

  docViewerModalOverlay.classList.add("open");
};

window.downloadDoc = function(attachment) {
  DocumentEngine.download(attachment);
};

// ================= TAB 4: PROFILE & FILES =================
function renderProfile() {
  document.getElementById("profileNameDisplay").textContent = state.profile.name;
  document.getElementById("profileCompanyDisplay").textContent = state.profile.company;
  document.getElementById("profileTierDisplay").textContent = state.profile.tier;
  document.getElementById("profileEmailDisplay").textContent = state.profile.email;
  document.getElementById("profilePhoneDisplay").textContent = state.profile.phone;
  document.getElementById("profileAddressDisplay").textContent = state.profile.shippingAddress;

  // Stats
  document.getElementById("profileOrdersCount").textContent = state.orders.length;
  document.getElementById("profileServicesCount").textContent = state.services.length;
  const totalSpent = state.orders.reduce((acc, o) => acc + o.totalAmount, 0);
  document.getElementById("profileTotalSpent").textContent = `$${totalSpent.toFixed(2)}`;

  // Centralized Files
  const allFiles = [
    ...state.orders.flatMap(o => o.attachments || []),
    ...state.services.flatMap(s => s.attachments || [])
  ];

  const grid = document.getElementById("centralFilesGrid");
  if (allFiles.length === 0) {
    grid.innerHTML = `<p class="section-desc">No files uploaded yet.</p>`;
  } else {
    grid.innerHTML = allFiles.map(file => renderAttachmentRow(file)).join("");
  }
}

// ================= TAB 5: ADMIN DASHBOARD =================
function renderAdmin() {
  // Metrics
  const pendingOrders = state.orders.filter(o => o.status === "PENDING").length;
  const pendingServices = state.services.filter(s => s.status === "SUBMITTED" || s.status === "REVIEW").length;
  const lowStock = state.products.filter(p => p.stock <= p.minAlert).length;
  const gross = state.orders.reduce((acc, o) => acc + o.totalAmount, 0);

  document.getElementById("adminPendingOrders").textContent = pendingOrders;
  document.getElementById("adminPendingServices").textContent = pendingServices;
  document.getElementById("adminLowStockCount").textContent = lowStock;
  document.getElementById("adminTotalRevenue").textContent = `$${gross.toFixed(0)}`;

  document.getElementById("adminOrdersCountBadge").textContent = state.orders.length;
  document.getElementById("adminServicesCountBadge").textContent = state.services.length;

  renderAdminInventory();
  renderAdminOrders();
  renderAdminServices();
}

function renderAdminInventory() {
  const tbody = document.getElementById("adminInventoryTableBody");
  tbody.innerHTML = state.products.map(prod => {
    const isOut = prod.stock <= 0;
    const isLow = prod.stock > 0 && prod.stock <= prod.minAlert;
    const badge = isOut
      ? '<span style="color:var(--danger); font-weight:700;">Out of Stock</span>'
      : (isLow ? '<span style="color:var(--warning); font-weight:700;">Low Stock</span>' : '<span style="color:var(--success); font-weight:700;">In Stock</span>');

    return `
      <tr>
        <td><strong>${prod.name}</strong></td>
        <td>${prod.category}</td>
        <td><code>${prod.sku}</code></td>
        <td>$${prod.price.toFixed(2)}</td>
        <td><strong>${prod.stock}</strong> ${prod.unit}</td>
        <td>${badge}</td>
        <td>
          <div style="display:flex; gap:0.25rem;">
            <button class="btn btn-outline btn-sm" onclick="window.adminAdjustStock('${prod.id}', ${prod.stock - 1})">-1</button>
            <button class="btn btn-outline btn-sm" onclick="window.adminAdjustStock('${prod.id}', ${prod.stock + 10})">+10</button>
            <button class="btn btn-primary btn-sm" onclick="window.adminAdjustStock('${prod.id}', ${prod.stock + 25})">+25</button>
          </div>
        </td>
      </tr>
    `;
  }).join("");
}

window.adminAdjustStock = function(id, newStock) {
  const prod = state.products.find(p => p.id === id);
  if (prod) {
    prod.stock = Math.max(0, newStock);
    state.persistAll();
    renderAdmin();
    showToast(`Stock updated for ${prod.name}`);
  }
};

function renderAdminOrders() {
  const list = document.getElementById("adminOrdersList");
  list.innerHTML = state.orders.map(order => `
    <div class="card" style="background:#fff; border:1px solid #E2E8F0; border-radius:12px; padding:1.25rem;">
      <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.75rem;">
        <div>
          <strong>${order.id}</strong> • Client: ${order.customerName}
          <div style="font-size:0.75rem; color:#64748B;">Total: $${order.totalAmount.toFixed(2)} • Destination: ${order.shippingAddress}</div>
        </div>
        <span class="status-badge badge-${order.status.toLowerCase()}">${order.status}</span>
      </div>

      <div style="display:flex; gap:0.5rem; flex-wrap:wrap; margin-top:0.75rem;">
        ${order.status === 'PENDING' ? `
          <button class="btn btn-secondary btn-sm" onclick="window.adminAdvanceOrder('${order.id}', 'APPROVED', 1)">Approve Order</button>
        ` : ''}
        ${order.status === 'APPROVED' ? `
          <button class="btn btn-primary btn-sm" onclick="window.adminAdvanceOrder('${order.id}', 'IN_PROGRESS', 2)">Mark In Production</button>
        ` : ''}
        ${order.status === 'IN_PROGRESS' ? `
          <button class="btn btn-primary btn-sm" onclick="window.adminAdvanceOrder('${order.id}', 'READY_FOR_DELIVERY', 3)">Ready for Dispatch</button>
        ` : ''}
        ${order.status === 'READY_FOR_DELIVERY' ? `
          <button class="btn btn-secondary btn-sm" onclick="window.adminAdvanceOrder('${order.id}', 'DELIVERED', 4)">Mark Delivered</button>
        ` : ''}

        <button class="btn btn-outline btn-sm" onclick='window.openUploadModal("order", ${JSON.stringify(order)})'>
          + Upload Deliverable File
        </button>
      </div>

      ${order.attachments && order.attachments.length > 0 ? `
        <div style="margin-top:0.75rem; border-top:1px solid #F1F5F9; padding-top:0.5rem;">
          <div style="font-size:0.75rem; font-weight:700; color:var(--primary); margin-bottom:0.35rem;">Attached Deliverables:</div>
          ${order.attachments.map(att => renderAttachmentRow(att)).join("")}
        </div>
      ` : ''}
    </div>
  `).join("");
}

window.adminAdvanceOrder = function(orderId, newStatus, newIdx) {
  const order = state.orders.find(o => o.id === orderId);
  if (order) {
    order.status = newStatus;
    order.statusIndex = newIdx;
    state.persistAll();
    renderAdmin();
    showToast(`Order ${orderId} moved to ${newStatus}`);
  }
};

function renderAdminServices() {
  const list = document.getElementById("adminServicesList");
  list.innerHTML = state.services.map(srv => `
    <div class="card" style="background:#fff; border:1px solid #E2E8F0; border-radius:12px; padding:1.25rem;">
      <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.5rem;">
        <div>
          <strong style="color:var(--secondary);">${srv.id}</strong> • <strong>${srv.serviceType}</strong>
          <div style="font-size:0.75rem; color:#64748B;">Client: ${srv.customerName} • Urgency: ${srv.urgency} • Est: $${srv.budget}</div>
        </div>
        <span class="status-badge badge-${srv.status.toLowerCase()}">${srv.status}</span>
      </div>

      <p style="font-size:0.85rem; color:#334155; margin-bottom:0.75rem;">Requirements: ${srv.requirements}</p>

      <div style="display:flex; gap:0.5rem; flex-wrap:wrap;">
        ${srv.status === 'SUBMITTED' ? `
          <button class="btn btn-primary btn-sm" onclick="window.adminAdvanceService('${srv.id}', 'REVIEW', 1)">Mark Under Review</button>
        ` : ''}
        ${srv.status === 'REVIEW' ? `
          <button class="btn btn-secondary btn-sm" onclick="window.adminAdvanceService('${srv.id}', 'APPROVED', 2)">Approve & Schedule</button>
        ` : ''}
        ${srv.status === 'APPROVED' ? `
          <button class="btn btn-primary btn-sm" onclick="window.adminAdvanceService('${srv.id}', 'IN_EXECUTION', 3)">Start Execution</button>
        ` : ''}
        ${srv.status === 'IN_EXECUTION' ? `
          <button class="btn btn-secondary btn-sm" onclick="window.adminAdvanceService('${srv.id}', 'COMPLETED', 4)">Verify & Complete</button>
        ` : ''}

        <button class="btn btn-outline btn-sm" onclick='window.openUploadModal("service", ${JSON.stringify(srv)})'>
          + Upload Deliverable File
        </button>
      </div>

      ${srv.attachments && srv.attachments.length > 0 ? `
        <div style="margin-top:0.75rem; border-top:1px solid #F1F5F9; padding-top:0.5rem;">
          <div style="font-size:0.75rem; font-weight:700; color:var(--secondary); margin-bottom:0.35rem;">Service Certifications:</div>
          ${srv.attachments.map(att => renderAttachmentRow(att)).join("")}
        </div>
      ` : ''}
    </div>
  `).join("");
}

window.adminAdvanceService = function(srvId, newStatus, newIdx) {
  const srv = state.services.find(s => s.id === srvId);
  if (srv) {
    srv.status = newStatus;
    srv.statusIndex = newIdx;
    state.persistAll();
    renderAdmin();
    showToast(`Service ${srvId} updated to ${newStatus}`);
  }
};

// ================= ADMIN FILE UPLOAD HANDLER =================
window.openUploadModal = function(type, item) {
  currentUploadTarget = { type, item };
  document.getElementById("uploadTargetSubtitle").textContent = `For ${type.toUpperCase()} ID: ${item.id} (${item.customerName})`;
  document.getElementById("uploadDocTitle").value = type === "order" ? "Commercial Tax Invoice & Packing Slip" : "Quality Inspection & QA Certificate";
  uploadFileModalOverlay.classList.add("open");
};

function handleUploadFileSubmit(e) {
  e.preventDefault();
  if (!currentUploadTarget) return;

  const fileType = document.querySelector('input[name="uploadType"]:checked').value;
  const title = document.getElementById("uploadDocTitle").value;
  const remarks = document.getElementById("uploadDocRemarks").value || "Authorized and verified by Admin.";
  const fileInput = document.getElementById("uploadFileInput");

  const newAttachment = {
    id: "ATT-" + Math.floor(100 + Math.random() * 900),
    fileName: `${title.replace(/[^a-zA-Z0-9]/g, "_")}_${currentUploadTarget.item.id}.${fileType.toLowerCase()}`,
    fileType,
    sizeKb: fileInput.files[0] ? Math.round(fileInput.files[0].size / 1024) : (fileType === "PDF" ? 165 : 420),
    uploadedAt: Date.now(),
    desc: remarks
  };

  if (currentUploadTarget.type === "order") {
    const order = state.orders.find(o => o.id === currentUploadTarget.item.id);
    if (order) order.attachments.unshift(newAttachment);
  } else {
    const srv = state.services.find(s => s.id === currentUploadTarget.item.id);
    if (srv) srv.attachments.unshift(newAttachment);
  }

  state.persistAll();
  uploadFileModalOverlay.classList.remove("open");
  showToast(`File ${newAttachment.fileName} uploaded and delivered to client!`);
  renderAdmin();
}

// ================= EVENT LISTENERS =================
document.addEventListener("DOMContentLoaded", () => {
  // Navigation Tabs
  document.querySelectorAll(".nav-btn").forEach(btn => {
    btn.onclick = () => activateTab(btn.dataset.tab);
  });

  // Filter Pills (Tracking)
  document.querySelectorAll(".filter-pills .pill-btn").forEach(btn => {
    btn.onclick = () => {
      document.querySelectorAll(".filter-pills .pill-btn").forEach(b => b.classList.remove("active"));
      btn.classList.add("active");
      renderTracking();
    };
  });

  // Admin Subtabs
  document.querySelectorAll(".admin-tab-btn").forEach(btn => {
    btn.onclick = () => {
      document.querySelectorAll(".admin-tab-btn").forEach(b => b.classList.remove("active"));
      document.querySelectorAll(".admin-subpane").forEach(p => p.classList.remove("active"));
      btn.classList.add("active");
      document.getElementById(`admin${capitalize(btn.dataset.subtab)}Subtab`).classList.add("active");
    };
  });

  // Role toggle
  roleToggleBtn.onclick = () => {
    switchRole(state.currentRole === "CUSTOMER" ? "ADMIN" : "CUSTOMER");
  };

  // Cart
  openCartBtn.onclick = openCartDrawer;
  document.getElementById("closeCartBtn").onclick = () => cartDrawerOverlay.classList.remove("open");
  document.getElementById("checkoutSubmitBtn").onclick = submitOrder;

  // Services Modal
  document.getElementById("openServiceModalBtn").onclick = () => {
    document.getElementById("srvFormTitle").value = "Custom Engineering Fabrication";
    serviceModalOverlay.classList.add("open");
  };
  document.getElementById("closeServiceModalBtn").onclick = () => serviceModalOverlay.classList.remove("open");
  document.getElementById("cancelServiceModalBtn").onclick = () => serviceModalOverlay.classList.remove("open");
  document.getElementById("serviceRequestForm").onsubmit = submitServiceRequest;

  // Admin Add Product
  document.getElementById("adminAddProductBtn").onclick = () => {
    productModalOverlay.classList.add("open");
  };
  document.getElementById("closeProductModalBtn").onclick = () => productModalOverlay.classList.remove("open");
  document.getElementById("cancelProductModalBtn").onclick = () => productModalOverlay.classList.remove("open");
  document.getElementById("productForm").onsubmit = (e) => {
    e.preventDefault();
    const newProd = {
      id: "PROD-" + Math.floor(100 + Math.random() * 900),
      name: document.getElementById("prodFormName").value,
      category: document.getElementById("prodFormCategory").value,
      sku: document.getElementById("prodFormSku").value,
      price: parseFloat(document.getElementById("prodFormPrice").value),
      stock: parseInt(document.getElementById("prodFormStock").value),
      minAlert: parseInt(document.getElementById("prodFormMinAlert").value),
      description: document.getElementById("prodFormDesc").value,
      unit: "units"
    };
    state.products.unshift(newProd);
    state.persistAll();
    productModalOverlay.classList.remove("open");
    renderAdmin();
    showToast(`Added ${newProd.name} to inventory`);
  };

  // Upload Modal
  document.getElementById("closeUploadModalBtn").onclick = () => uploadFileModalOverlay.classList.remove("open");
  document.getElementById("cancelUploadModalBtn").onclick = () => uploadFileModalOverlay.classList.remove("open");
  document.getElementById("uploadFileForm").onsubmit = handleUploadFileSubmit;

  // Document Viewer Modal
  document.getElementById("closeDocViewerBtn").onclick = () => docViewerModalOverlay.classList.remove("open");
  document.getElementById("docViewerDownloadBtn").onclick = () => {
    if (activeDocAttachment) DocumentEngine.download(activeDocAttachment);
  };
  document.getElementById("docViewerPrintBtn").onclick = () => {
    window.print();
  };

  // Profile Edit
  document.getElementById("editProfileBtn").onclick = () => {
    document.getElementById("profileDetailsView").classList.add("hidden");
    document.getElementById("editProfileForm").classList.remove("hidden");
    document.getElementById("editNameInput").value = state.profile.name;
    document.getElementById("editCompanyInput").value = state.profile.company;
    document.getElementById("editEmailInput").value = state.profile.email;
    document.getElementById("editPhoneInput").value = state.profile.phone;
    document.getElementById("editAddressInput").value = state.profile.shippingAddress;
  };

  document.getElementById("cancelProfileBtn").onclick = () => {
    document.getElementById("profileDetailsView").classList.remove("hidden");
    document.getElementById("editProfileForm").classList.add("hidden");
  };

  document.getElementById("editProfileForm").onsubmit = (e) => {
    e.preventDefault();
    state.profile.name = document.getElementById("editNameInput").value;
    state.profile.company = document.getElementById("editCompanyInput").value;
    state.profile.email = document.getElementById("editEmailInput").value;
    state.profile.phone = document.getElementById("editPhoneInput").value;
    state.profile.shippingAddress = document.getElementById("editAddressInput").value;
    state.persistAll();
    document.getElementById("profileDetailsView").classList.remove("hidden");
    document.getElementById("editProfileForm").classList.add("hidden");
    renderProfile();
    showToast("Profile details updated");
  };

  // Search input
  document.getElementById("catalogSearchInput").oninput = renderCatalog;

  // Firebase Config Modal
  const firebaseConfigBtn = document.getElementById("firebaseConfigBtn");
  firebaseConfigBtn.onclick = () => {
    document.getElementById("firebaseConfigInput").value = state.firebaseConfig ? JSON.stringify(state.firebaseConfig, null, 2) : "";
    firebaseModalOverlay.classList.add("open");
  };
  document.getElementById("closeFirebaseModalBtn").onclick = () => firebaseModalOverlay.classList.remove("open");
  document.getElementById("clearFirebaseBtn").onclick = () => {
    state.firebaseConfig = null;
    state.save("biz_firebase_config", null);
    document.getElementById("firebaseDot").classList.remove("connected");
    document.getElementById("firebaseStatusText").textContent = "Local Storage";
    firebaseModalOverlay.classList.remove("open");
    showToast("Reset to offline-first local storage");
  };
  document.getElementById("saveFirebaseBtn").onclick = () => {
    try {
      const raw = document.getElementById("firebaseConfigInput").value;
      if (raw.trim()) {
        const config = JSON.parse(raw);
        state.firebaseConfig = config;
        state.save("biz_firebase_config", config);
        document.getElementById("firebaseDot").classList.add("connected");
        document.getElementById("firebaseStatusText").textContent = "Firebase Connected";
        showToast("Connected to Firebase Cloud Firestore!");
      }
    } catch (err) {
      alert("Invalid JSON format for Firebase configuration.");
    }
    firebaseModalOverlay.classList.remove("open");
  };

  // Initial render
  updateCartBadge();
  activateTab("catalog");
  renderServices();
  if (state.firebaseConfig) {
    document.getElementById("firebaseDot").classList.add("connected");
    document.getElementById("firebaseStatusText").textContent = "Firebase Connected";
  }
});

function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}
