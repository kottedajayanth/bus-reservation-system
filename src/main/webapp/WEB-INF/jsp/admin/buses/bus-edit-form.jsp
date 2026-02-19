<%@ page contentType="text/html; charset=UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8">
        <title>Edit Bus</title>
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
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.25);
            width: 100%;
            max-width: 650px;
          }

          .form-control::placeholder {
            color: #9aa0a6;
            font-size: 14px;
          }

          .days-box {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 10px;
            border: 1px solid #dee2e6;
          }

          .form-check {
            margin-bottom: 8px;
          }

          footer {
            background: rgba(0, 0, 0, 0.85);
            color: #fff;
            text-align: center;
            padding: 12px;
          }
        </style>
      </head>

      <body>

        <main>
          <div class="form-card">

            <h2 class="text-center mb-4">🚌 Edit Bus Details</h2>

            <form method="post" action="/admin/buses/update/${bus.busId}">

              <!-- Bus Name -->
              <div class="mb-3">
                <label class="form-label">Bus Name</label>
                <input name="busName" value="${bus.busName}" placeholder="Example: Z Travels Express" required
                  class="form-control" />
              </div>

              <!-- Driver -->
              <div class="mb-3">
                <label class="form-label">Driver Name</label>
                <input name="driverName" value="${bus.driverName}" placeholder="Driver full name" required
                  class="form-control" />
              </div>

              <!-- Type -->
              <div class="mb-3">
                <label class="form-label">Bus Type</label>
                <input name="busType" value="${bus.busType}" placeholder="AC Sleeper / Volvo / Non-AC" required
                  class="form-control" />
              </div>

              <!-- Seats -->
              <div class="mb-3">
                <label class="form-label">Total Seats</label>
                <input name="seats" type="number" value="${bus.seats}" placeholder="Total seating capacity" required
                  class="form-control" />
              </div>

              <!-- Available Seats -->
              <div class="mb-3">
                <label class="form-label">Available Seats</label>
                <input name="availableSeats" type="number" value="${bus.availableSeats}"
                  placeholder="Seats currently available" required class="form-control" />
              </div>

              <!-- Price -->
              <div class="mb-3">
                <label class="form-label">Ticket Price (₹)</label>
                <input name="price" type="number" step="0.01" value="${bus.price}" placeholder="Price per passenger"
                  required class="form-control" />
              </div>

              <!-- Route -->
              <div class="mb-3">
                <label class="form-label">Route</label>
                <select name="routeId" required class="form-select">
                  <c:forEach var="r" items="${routes}">
                    <option value="${r.routeId}" <c:if test="${bus.route.routeId == r.routeId}">selected</c:if>>
                      ${r.routeFrom} → ${r.routeTo}
                    </option>
                  </c:forEach>
                </select>
              </div>

              <!-- ================= RUNNING DAYS ================= -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Running Days</label>

                <div class="days-box">

                  <c:set var="days" value="${bus.runningDays}" />

                  <div class="row">

                    <div class="col-6 col-md-4 form-check">
                      <input class="form-check-input" type="checkbox" name="runningDays" value="MON" <c:if
                        test="${fn:contains(days,'MON')}">checked</c:if>>
                      <label class="form-check-label">Monday</label>
                    </div>

                    <div class="col-6 col-md-4 form-check">
                      <input class="form-check-input" type="checkbox" name="runningDays" value="TUE" <c:if
                        test="${fn:contains(days,'TUE')}">checked</c:if>>
                      <label class="form-check-label">Tuesday</label>
                    </div>

                    <div class="col-6 col-md-4 form-check">
                      <input class="form-check-input" type="checkbox" name="runningDays" value="WED" <c:if
                        test="${fn:contains(days,'WED')}">checked</c:if>>
                      <label class="form-check-label">Wednesday</label>
                    </div>

                    <div class="col-6 col-md-4 form-check">
                      <input class="form-check-input" type="checkbox" name="runningDays" value="THU" <c:if
                        test="${fn:contains(days,'THU')}">checked</c:if>>
                      <label class="form-check-label">Thursday</label>
                    </div>

                    <div class="col-6 col-md-4 form-check">
                      <input class="form-check-input" type="checkbox" name="runningDays" value="FRI" <c:if
                        test="${fn:contains(days,'FRI')}">checked</c:if>>
                      <label class="form-check-label">Friday</label>
                    </div>

                    <div class="col-6 col-md-4 form-check">
                      <input class="form-check-input" type="checkbox" name="runningDays" value="SAT" <c:if
                        test="${fn:contains(days,'SAT')}">checked</c:if>>
                      <label class="form-check-label">Saturday</label>
                    </div>

                    <div class="col-6 col-md-4 form-check">
                      <input class="form-check-input" type="checkbox" name="runningDays" value="SUN" <c:if
                        test="${fn:contains(days,'SUN')}">checked</c:if>>
                      <label class="form-check-label">Sunday</label>
                    </div>

                  </div>
                </div>
              </div>

              <!-- Buttons -->
              <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary">
                  ✅ Update Bus
                </button>

                <a href="/admin/buses" class="btn btn-outline-secondary">
                  Cancel
                </a>
              </div>

            </form>
          </div>
        </main>

        <footer>
          © 2026 Bus Reservation System | Edit Bus
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
      </body>

      </html>