function init() {
    setComboMailValue("${mail2}");
    setComboBirthValue("${month}");
}

function setComboMailValue(val) {
    var selectMail = document.getElementById('mail2');
    for (i = 0, j = selectMail.length; i < j; i++) {
        if (selectMail.options[i].value == val) {
            selectMail.options[i].selected = true;
            break;
        }
    }
}
function setComboBirthValue(val) {
    var selectBirth = document.getElementById('birthmm');
    for (i = 0, j = selectBirth.length; i < j; i++){
        if (selectBirth.options[i].value == val){
            selectBirth.options[i].selected = true;
            break;
        }
    }
}
function checkForm() {
    if (!document.newMember.id.value) {
        alert("아이디를 입력하세요.");
        return false;
    }
    if (!document.newMember.password.value) {
        alert("비밀번호를 입력하세요.");
        return false;
    }
    if (document.newMember.password.value != document.newMember.password_confirm.value) {
        alert("비밀번호를 동일하게 입력하세요.");
        return false;
    }
}
