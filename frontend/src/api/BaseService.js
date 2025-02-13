import axios from 'axios';

class BaseService {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
    }

    getAuthHeaders() {
        const token = localStorage.getItem("token");
        return {
            headers: {
                Authorization: token ? `Bearer ${token}` : "",
            },
        };
    }

    getAll() {
        return axios.get(`${this.baseUrl}/get/all`, this.getAuthHeaders());
    }

    getById(id) {
        return axios.get(`${this.baseUrl}/get/${id}`, this.getAuthHeaders());
    }

    insert(data) {
        return axios.post(`${this.baseUrl}/insert`, data, this.getAuthHeaders());
    }

    update(id, data) {
        return axios.put(`${this.baseUrl}/update/${id}`, data, this.getAuthHeaders());
    }

    delete(id) {
        return axios.delete(`${this.baseUrl}/delete/${id}`, this.getAuthHeaders());
    }
}

export default BaseService;