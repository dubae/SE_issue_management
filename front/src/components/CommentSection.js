import React from 'react';
import PropTypes from 'prop-types';
import './CommentSection.css';

const CommentSection = ({ comments, onUserClick }) => {
    return (
        <div className="comment-section">
            <h2>코멘트</h2>
            {comments.map((comment, index) => (
                <div key={index} className="comment">
                    <p>
                        <span className="comment-user comment-user-link" onClick={() => onUserClick(comment.writerId)}>
                            [{comment.writerId}]
                        </span>
                        {` ${comment.content} ${comment.createdAt}`}
                    </p>
                </div>
            ))}
        </div>
    );
};

CommentSection.propTypes = {
    comments: PropTypes.array.isRequired,
    onUserClick: PropTypes.func.isRequired,
};

export default CommentSection;
