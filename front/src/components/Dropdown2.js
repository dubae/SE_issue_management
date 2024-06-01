import React from 'react';

function Dropdown2({ label, name, options, value, onChange }) {
    return (
        <div className="dropdown">
            <label>{label}</label>
            <select name={name} value={value} onChange={onChange}>
                {options.map(option => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
        </div>
    );
}

export default Dropdown2;