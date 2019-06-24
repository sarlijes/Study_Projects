const helloWorld = "Hello World in a frame"
const nameAndEMail = "Jessica Sarlin jessica.sarlin(at)gmail.com"
const instruction = "JavaScript-function that prints words"

const frameAnyString = (inputString) => {

    let longestWord = inputString
        .split(' ')
        .reduce((a, b) => a.length < b.length ? b : a, "");

    console.log("*".repeat(longestWord.length + 4));

    inputString
        .split(' ')
        .map(word => word.padEnd(longestWord.length, " "))
        .map(word => console.log("*", word, "*"))

    console.log("*".repeat(longestWord.length + 4));
}
frameAnyString(helloWorld)

/* Inputs:
*********
* Hello *
* World *
* in    *
* a     *
* frame *
*********
*/

frameAnyString(nameAndEMail)

/* Inputs:
*******************************
* Jessica                     *
* Sarlin                      *
* jessica.sarlin(at)gmail.com *
*******************************
*/

frameAnyString(instruction)

/* Inputs:
***********************
* JavaScript-function *
* that                *
* prints              *
* words               *
***********************
*/



