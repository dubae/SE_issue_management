import React from 'react';
import { Form } from 'react-bootstrap';

const Dropdown = ({ label, options, value, onChange, multiple = false }) => {
    return (
        <div className="dropdown-section">
            <h3>{label}</h3>
            <Form.Control as="select" value={value} onChange={onChange} multiple={multiple}>
                {options.map(option => (
                    <option key={option.value} value={option.value}>{option.label}</option>
                ))}
            </Form.Control>
        </div>
    );
};

export default Dropdown;
