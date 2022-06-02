import axios from "axios";
import configData from "../config.json";
import {getCookie, goto} from "../utils/utils";

const endpoint = configData.SERVER_URL+"/github/connect";

export function connectToGithub(event, setSuccessful) {
    console.log(event.currentTarget)
    const data = new FormData(event.currentTarget);


    axios.post(`${endpoint}`, null, {
        headers: {
            'Personal-Access-Token':data.get('github'),
            'Authorization':getCookie('Authorization'+configData.COOKIE_SUFFIX),
            'Content-Type': 'application/json',
        }
    }).then(
        (res) => {
            setSuccessful(true);
            console.log(res.data);
            goto("/");
        }
        ,
        (err) => {
            //TODO : handle error with customized stuff
            alert("erreur lors de l'authentification, veuillez reentrer vos données, en cas de besoin contacter l'admin");
            console.error(err);
        }
    );

}