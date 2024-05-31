import axios from 'axios';

const API_URL = 'http://localhost:4440/api';

export const register = async (memberData) => {
    try {
        const response = await axios.post(`${API_URL}/register`,
            {
                'userid': memberData.userid,
                'username': memberData.username,
                'email': memberData.email,
                'password': memberData.password,
            },
            {
            'Access-Control-Allow-Origin': 'http://localhost:4440'
            }
            );
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};