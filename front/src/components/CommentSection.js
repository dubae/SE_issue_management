import React from 'react';

const CommentSection = ({ comments }) => {
    return (
        <div className="comment-section">
            <h2>코멘트</h2>
            {comments.map((comment, index) => (
                <div key={index} className="comment">
                    <p>{`[${comment.author}] ${comment.content} ${comment.date}`}</p>
                </div>
            ))}
        </div>
    );
};

export default CommentSection;
