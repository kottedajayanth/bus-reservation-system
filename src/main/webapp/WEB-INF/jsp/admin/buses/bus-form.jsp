<%@ page contentType="text/html; charset=UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="UTF-8">
      <title>Add Bus</title>
      <meta name="viewport" content="width=device-width, initial-scale=1" />

      <!-- Bootstrap -->
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

      <style>
        body {
          background-image: url('https://source.unsplash.com/1600x900/?bus,road,travel');
          background-size: cover;
          background-position: center;
          background-attachment: fixed;
          font-family: 'Segoe UI', sans-serif;
          min-height: 100vh;
          display: flex;
          flex-direction: column;
        }

        main {
          flex: 1;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .form-card {
          background: rgba(255, 255, 255, 0.96);
          padding: 35px;
          border-radius: 14px;
          box-shadow: 0 10px 28px rgba(0, 0, 0, .25);
          width: 100%;
          max-width: 650px;
        }

        .form-label {
          font-weight: 600;
        }

        .form-text {
          font-size: 13px;
          color: #6c757d;
        }

        /* ===== RUNNING DAYS PILLS ===== */

        .day-box {
          display: flex;
          flex-wrap: wrap;
          gap: 10px;
        }

        .day-box label {
          cursor: pointer;
        }

        .day-pill {
          padding: 8px 16px;
          border-radius: 9px;
          border: 1px solid #dee2e6;
          background: #f8f9fa;
          transition: all .2s ease;
          font-weight: 500;
        }

        .day-box input {
          display: none;
        }

        .day-box input:checked+.day-pill {
          background: #0d6efd;
          color: white;
          border-color: #0d6efd;
          box-shadow: 0 3px 8px rgba(13, 110, 253, .35);
          transform: translateY(-1px);
        }

        footer {
          background: rgba(0, 0, 0, .85);
          color: #fff;
          text-align: center;
          padding: 12px;
        }
      </style>
    </head>

    <body>

      <main>
        <div class="form-card">

          <h2 class="text-center mb-4">🚌 Add New Bus</h2>

          <div class="mb-3 text-end">
            <a href="/admin/buses" class="btn btn-secondary btn-sm">
              ← Back to Bus List
            </a>
          </div>

          <form method="post" action="/admin/buses">

            <!-- BUS NAME -->
            <div class="mb-3">
              <label class="form-label">Bus Name</label>
              <input name="busName" required class="form-control"
                placeholder="Example: Orange Travels / APSRTC / VRL" />
              <div class="form-text">Company or service name.</div>
            </div>

            <!-- DRIVER -->
            <div class="mb-3">
              <label class="form-label">Driver Name</label>
              <input name="driverName" required class="form-control" placeholder="Driver full name" />
            </div>

            <!-- TYPE -->
            <div class="mb-3">
              <label class="form-label">Bus Type</label>
              <input name="busType" required class="form-control" placeholder="AC Sleeper / Non-AC Seater / Volvo" />
            </div>

            <!-- SEATS -->
            <div class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label">Total Seats</label>
                <input name="seats" type="number" required class="form-control" placeholder="Example: 40" />
              </div>

              <div class="col-md-6 mb-3">
                <label class="form-label">Available Seats</label>
                <input name="availableSeats" type="number" required class="form-control"
                  placeholder="Usually same as total seats" />
              </div>
            </div>

            <!-- PRICE -->
            <div class="mb-3">
              <label class="form-label">Ticket Price (₹)</label>
              <input name="price" type="number" step="0.01" required class="form-control"
                placeholder="Example: 599.00" />
              <div class="form-text">Price per passenger.</div>
            </div>

            <!-- ================= RUNNING DAYS ================= -->
            <div class="mb-4">
              <label class="form-label fw-semibold">Running Days</label>

              <div class="day-box">

                <label>
                  <input type="checkbox" name="runningDays" value="MON">
                  <span class="day-pill">Mon</span>
                </label>

                <label>
                  <input type="checkbox" name="runningDays" value="TUE">
                  <span class="day-pill">Tue</span>
                </label>

                <label>
                  <input type="checkbox" name="runningDays" value="WED">
                  <span class="day-pill">Wed</span>
                </label>

                <label>
                  <input type="checkbox" name="runningDays" value="THU">
                  <span class="day-pill">Thu</span>
                </label>

                <label>
                  <input type="checkbox" name="runningDays" value="FRI">
                  <span class="day-pill">Fri</span>
                </label>

                <label>
                  <input type="checkbox" name="runningDays" value="SAT">
                  <span class="day-pill">Sat</span>
                </label>

                <label>
                  <input type="checkbox" name="runningDays" value="SUN">
                  <span class="day-pill">Sun</span>
                </label>

              </div>

              <div class="form-text">
                Select days when this bus operates.
              </div>
            </div>

            <!-- ROUTE -->
            <div class="mb-4">
              <label class="form-label">Route</label>
              <select name="routeId" required class="form-select">
                <c:forEach var="r" items="${routes}">
                  <option value="${r.routeId}">
                    ${r.routeFrom} → ${r.routeTo}
                  </option>
                </c:forEach>
              </select>
            </div>

            <button type="submit" class="btn btn-primary w-100">
              💾 Save Bus
            </button>

          </form>
        </div>
      </main>

      <footer>
        © 2026 Bus Reservation System | Admin Panel
      </footer>

      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>

    </html>