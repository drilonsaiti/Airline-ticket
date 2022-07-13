
function maxLengthCheck(object) {
    if (object.value.length > object.maxLength)
        object.value = object.value.slice(0, object.maxLength)
}
function minLengthCheck(object) {
    if (object.value.length < object.maxLength)
        object.value = object.value.slice(0, object.maxLength)
}

function lengthRange(inputtxt, minlength, maxlength)
{
    var userInput = inputtxt.value;
    if(userInput.length >= minlength && userInput.length <= maxlength)
    {
        return true;
    }
    else
    {
        alert("Please enter 12 numbers of credit card");
        return false;
    }
}

function checkInput(elem){
    if(elem.value.length != 4){
        alert("This value needs to be 4 characters long!");
        elem.value = ""; // Reset the textbox
    }
    else{
        alert("This value is 4 characters long.");
    }
}

function selectFirst(){
    $('select option:first-child')
}