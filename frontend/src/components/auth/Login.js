import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import { StartTokenAutoRefresh } from './TokenRefresh';

const clientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;

function Login() {

    const navigate = useNavigate();

    const onSuccess = async (response) => {
        try {
            const res = await axios.get(process.env.REACT_APP_BACKEND_URL + "/auth/user", {
                headers: {
                    Google: `Bearer ${response.credential}`,
                },
            });
            const { token, refreshToken } = res.data;

            localStorage.setItem("token", token);
            localStorage.setItem("refreshToken", refreshToken);
            StartTokenAutoRefresh(navigate);

            navigate(`/`);
        } catch (error) {
            console.error("Error fetching user data:", error);
            if (error.response) {
                console.error("Response data:", error.response.data);
                console.error("Response status:", error.response.status);
                console.error("Response headers:", error.response.headers);
            } else if (error.request) {
                console.error("Request:", error.request);
            } else {
                console.error("Error message:", error.message);
            }

        }
    };

    const onFailure = (error) => {
        console.error("Login Failed:", error);
    };

    return (
        <GoogleOAuthProvider clientId={clientId}>
            <GoogleLogin
                onSuccess={onSuccess}
                onError={onFailure}
                theme="filled_blue"
                size="large"
            />
        </GoogleOAuthProvider>
    );
}

export default Login;
