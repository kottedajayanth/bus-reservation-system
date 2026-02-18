<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


    <!-- ================= TOAST STYLES ================= -->
    <style>
      /* Toast animation */
      .toast {
        border-radius: 12px;
        box-shadow: 0 8px 22px rgba(0, 0, 0, 0.25);
        animation: toastSlide 0.35s ease;
      }

      @keyframes toastSlide {
        from {
          opacity: 0;
          transform: translateX(40px);
        }

        to {
          opacity: 1;
          transform: translateX(0);
        }
      }

      /* Modern white toast */
      .custom-toast {
        background: #ffffff !important;
        color: #212529 !important;
        border-radius: 12px;
        box-shadow: 0 10px 28px rgba(0, 0, 0, 0.18);
        min-width: 300px;
        border-left: 5px solid transparent;
      }

      .custom-toast .toast-body {
        font-weight: 500;
      }

      /* Types */
      .success-toast {
        border-left-color: #198754;
      }

      .error-toast {
        border-left-color: #dc3545;
      }

      .warning-toast {
        border-left-color: #f59e0b;
      }

      /* Icons */
      .success-icon {
        color: #198754;
        font-size: 18px;
        margin-right: 10px;
      }

      .error-icon {
        color: #dc3545;
        font-size: 18px;
        margin-right: 10px;
      }

      .warning-icon {
        color: #f59e0b;
        font-size: 18px;
        margin-right: 10px;
      }
    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">


    <!-- ================= NAVBAR ================= -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">

        <a class="navbar-brand" href="/">BusReserve</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">

          <!-- LEFT MENU -->
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item"><a class="nav-link" href="/">Home</a></li>
            <li class="nav-item"><a class="nav-link" href="/buses">Buses</a></li>
            <li class="nav-item"><a class="nav-link" href="/routes">Routes</a></li>
            <li class="nav-item"><a class="nav-link" href="/reservations">Reservations</a></li>
          </ul>

          <!-- RIGHT MENU -->
          <ul class="navbar-nav">

            <!-- ADMIN / USER / GUEST SWITCH -->
            <c:choose>

              <c:when test="${not empty sessionScope.admin}">
                <li class="nav-item">
                  <a class="nav-link" href="/admin">Admin Dashboard</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="/auth/logout">Logout</a>
                </li>
              </c:when>

              <c:when test="${not empty sessionScope.user}">
                <li class="nav-item">
                  <span class="navbar-text">
                    Welcome, ${sessionScope.user.firstName}
                  </span>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="/auth/logout">Logout</a>
                </li>
              </c:when>

              <c:otherwise>
                <li class="nav-item"><a class="nav-link" href="/auth/login-admin">Admin Login</a></li>
                <li class="nav-item"><a class="nav-link" href="/auth/login-user">User Login</a></li>
                <li class="nav-item"><a class="nav-link" href="/auth/register">Register</a></li>
              </c:otherwise>

            </c:choose>

          </ul>

        </div>
      </div>
    </nav>


    <!-- ================= GLOBAL TOAST ================= -->

    <div class="toast-container position-fixed p-3" style="top:75px; right:20px; z-index:1100;">

      <!-- SUCCESS -->
      <c:if test="${not empty message}">
        <div id="successToast" class="toast custom-toast success-toast">
          <div class="d-flex align-items-center p-3">
            <i class="bi bi-check-circle-fill success-icon"></i>

            <div class="toast-body p-0">${message}</div>
            <button type="button" class="btn-close ms-auto" data-bs-dismiss="toast"></button>
          </div>
        </div>
      </c:if>

      <!-- ERROR -->
      <c:if test="${not empty error}">
        <div id="errorToast" class="toast custom-toast error-toast">
          <div class="d-flex align-items-center p-3">
            <i class="bi bi-x-circle-fill error-icon"></i>
            <div class="toast-body p-0">${error}</div>
            <button type="button" class="btn-close ms-auto" data-bs-dismiss="toast"></button>
          </div>
        </div>
      </c:if>

      <!-- WARNING -->
      <c:if test="${not empty warning}">
        <div id="warningToast" class="toast custom-toast warning-toast">
          <div class="d-flex align-items-center p-3">
            <i class="bi bi-exclamation-triangle-fill warning-icon"></i>
            <div class="toast-body p-0">${warning}</div>
            <button type="button" class="btn-close ms-auto" data-bs-dismiss="toast"></button>
          </div>
        </div>
      </c:if>

    </div>


    <!-- ================= TOAST SCRIPT ================= -->
    <script>
      document.addEventListener("DOMContentLoaded", function () {

        function showToast(id, delay) {
          const el = document.getElementById(id);
          if (el) {
            const toast = new bootstrap.Toast(el, { delay: delay });
            toast.show();
          }
        }

        showToast("successToast", 2500);
        showToast("errorToast", 4000);
        showToast("warningToast", 3500);

      });
    </script>