import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const register = async (memberData) => {
    try {
        const response = await axios.post(`${API_URL}/register`, memberData);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};
