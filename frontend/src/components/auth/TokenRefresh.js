import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

let refreshTimerId = null;

async function StartTokenAutoRefresh(navigate) {

    const token = localStorage.getItem("token");
    const refreshToken = localStorage.getItem("refreshToken");

    CancelTokenAutoRefresh();

    if (!token || !refreshToken) {
        return;
    }

    const payload = jwtDecode(token);

    const isExpired = payload.exp * 1000 < Date.now();

    if (!payload?.exp || isExpired) {
        navigate(`/logout`);
        return;
    }

    const accessExp = payload.exp * 1000;
    const now = Date.now();

    const refreshDelay = accessExp - now - 2 * 60 * 1000;

    if (refreshDelay <= 0) {
        await RefreshToken(navigate, refreshToken);
        return;
    }

    refreshTimerId = setTimeout(async () => {
        await RefreshToken(navigate, refreshToken);
    }, refreshDelay);
}

async function RefreshToken(navigate, refreshToken) {
    try {
        const res = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/auth/refresh`, {
            refreshToken,
        });

        if (res.status === 200) {
            const { token: newToken, refreshToken: newRefreshToken } = res.data;
            localStorage.setItem("token", newToken);
            localStorage.setItem("refreshToken", newRefreshToken);

            StartTokenAutoRefresh(navigate);
        } else {
            throw new Error("Refresh failed");
        }
    } catch (err) {
        console.error("Failed to refresh token:", err);
        localStorage.clear();
        navigate(`/login`);
    }
}

function CancelTokenAutoRefresh() {
    if (refreshTimerId !== null) {
        clearTimeout(refreshTimerId);
        refreshTimerId = null;
    }
}

export { StartTokenAutoRefresh, CancelTokenAutoRefresh }