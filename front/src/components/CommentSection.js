import React from 'react';
import PropTypes from 'prop-types';
import './CommentSection.css';

const CommentSection = ({ comments, onUserClick }) => {
    return (
        <div className="comment-section">
            <h2>코멘트</h2>
            {comments.length > 0 ? (
                comments.map((comment, index) => (
                    <div key={index} className="comment">
                        <p>
                            <span className="comment-user comment-user-link" onClick={() => onUserClick(comment.writerId)}>
                                [{comment.writerId}]
                            </span>
                            {` ${comment.content} ${new Date(comment.createdAt).toLocaleString()}`}
                        </p>
                    </div>
                ))
            ) : (
                <p>코멘트가 없습니다.</p>
            )}
        </div>
    );
};

CommentSection.propTypes = {
    comments: PropTypes.arrayOf(
        PropTypes.shape({
            writerId: PropTypes.string.isRequired,
            content: PropTypes.string.isRequired,
            createdAt: PropTypes.string.isRequired, // Assuming createdAt is passed as an ISO string
        })
    ).isRequired,
    onUserClick: PropTypes.func.isRequired,
};

export default CommentSection;
