window.onload = function () {
    document.querySelector("#checkbox").addEventListener("change", showPass);
};

function showPass() {
    var x = document.querySelector("#pw");
  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }
}