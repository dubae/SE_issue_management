import React from 'react';
import { Button } from 'react-bootstrap';

const Pagination = ({ totalPages, currentPage, onPageChange }) => {
    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
        pageNumbers.push(i);
    }

    return (
        <div className="pagination">
            {pageNumbers.map(number => (
                <Button key={number} onClick={() => onPageChange(number)}>{number}</Button>
            ))}
        </div>
    );
};

export default Pagination;
