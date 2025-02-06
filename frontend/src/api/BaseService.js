import axios from 'axios';

class BaseService {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
    }

    getAll() {
        return axios.get(`${this.baseUrl}/get/all`);
    }

    getById(id) {
        return axios.get(`${this.baseUrl}/get/${id}`);
    }

    insert(data) {
        return axios.post(`${this.baseUrl}/insert`, data);
    }

    update(data, id) {
        return axios.put(`${this.baseUrl}/update/${id}`, data);
    }

    delete(id) {
        return axios.delete(`${this.baseUrl}/delete/${id}`);
    }
}

export default BaseService;