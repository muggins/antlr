/** \file
 * Defines the implementation of the common node stream the default
 * tree node stream used by ANTLR.
 */

#include    <antlr3commontreenodestream.h>

#ifdef	WIN32
#pragma warning( disable : 4100 )
#endif

static	void			    reset			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_BASE_TREE	    LT				(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 k);
static	pANTLR3_BASE_TREE	    getTreeSource		(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	void			    fill			(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 k);
static	void			    addLookahead		(pANTLR3_COMMON_TREE_NODE_STREAM ctns, pANTLR3_BASE_TREE node);
static	void			    consume			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	ANTLR3_UINT32		    LA				(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 i);
static	ANTLR3_UINT64		    mark			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	void			    release			(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 marker);
static	void			    rewindMark			(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 marker);
static	void			    rewindInput			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	void			    seek			(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 index);
static	ANTLR3_UINT64		    index			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	ANTLR3_UINT64		    size			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	ANTLR3_BOOLEAN		    hasNext			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_BASE_TREE	    next			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_BASE_TREE	    handleRootnode		(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_BASE_TREE	    visitChild			(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 child);
static	void			    addNavigationNode		(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT32 ttype);
static	pANTLR3_BASE_TREE	    newDownNode			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_BASE_TREE	    newUpNode			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	void			    walkBackToMostRecentNodeWithUnvisitedChildren   
								(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_BASE_TREE_ADAPTOR   getTreeAdaptor		(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	ANTLR3_BOOLEAN		    hasUniqueNavigationNodes	(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	void			    setUniqueNavigationNodes	(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_BOOLEAN uniqueNavigationNodes);
static	pANTLR3_STRING		    toNodesOnlyString		(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_STRING		    toString			(pANTLR3_COMMON_TREE_NODE_STREAM ctns);
static	pANTLR3_STRING		    toStringSS			(pANTLR3_COMMON_TREE_NODE_STREAM ctns, pANTLR3_BASE_TREE start, pANTLR3_BASE_TREE stop);
static	void			    toStringWork		(pANTLR3_COMMON_TREE_NODE_STREAM ctns, pANTLR3_BASE_TREE start, pANTLR3_BASE_TREE stop, pANTLR3_STRING buf);
static	ANTLR3_UINT32		    getLookaheadSize		(pANTLR3_COMMON_TREE_NODE_STREAM ctns);









/** Reset the input stream to the start of the input nodes.
 */
static	void		
reset	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    ctns->currentNode		= ctns->root;
    ctns->previousNode		= NULL;
    ctns->currentChildIndex	= -1;
    ctns->absoluteNodeIndex	= -1;
    ctns->head			= 0;
    ctns->tail			= 0;
}

/** Get tree node at current input pointer + i ahead where i=1 is next node.
 *  i<0 indicates nodes in the past.  So -1 is previous node and -2 is
 *  two nodes ago. LT(0) is undefined.  For i>=n, return null.
 *  Return null for LT(0) and any index that results in an absolute address
 *  that is negative.
 *
 *  This is analogus to the LT() method of the TokenStream, but this
 *  returns a tree node instead of a token.  Makes code gen identical
 *  for both parser and tree grammars. :)
 */
static	pANTLR3_BASE_TREE	    
LT	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 k)
{
    if	(k == -1)
    {
	return	ctns->previousNode;
    }
    else if	(k < 0)
    {
	fprintf(stderr, "Tree node streams may not look back more than 1 node!\n");
	return NULL;
    }
    else if	(k == 0)
    {
	return	&(ctns->INVALID_NODE);
    }

    /* k was a legitimate request, so we need to ensure we have at least
     * k nodes in the look ahead buffer.
     */
    ctns->fill(ctns, k);

    /* Return k tokens in front of the current head token, allowing for the fact
     * that k could wrap around the circular buffer.
     */
    return  *(ctns->lookAhead + ((ctns->head + k + 1) % ctns->lookAheadLength));
}

/** Where is this stream pulling nodes from?  This is not the name, but
 *  the object that provides node objects.
 */
static	pANTLR3_BASE_TREE	    
getTreeSource	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return  ctns->root;
}

/** Make sure we have at least k symbols in lookahead buffer 
 */
static	void		    
fill	(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 k)
{
    ANTLR3_UINT32	n;
    ANTLR3_UINT32	i;

    n = ctns->getLookaheadSize(ctns);

    /* We loop on the iterator function which will cause buffer expansion
     * and the tree nodes to be followed and read
     */
    for	(i=1; i<=(k-n); i++)
    {
	/* Cause the next node to be read in from the stream
	 */
	ctns->next(ctns);
    }
}

/** Add a node to the lookahead buffer.  Add at lookahead[tail].
 *  If you tail+1 == head, then we must create a bigger buffer
 *  and copy all the nodes over plus reset head, tail.  After
 *  this method, LT(1) will be lookahead[0].
 */
static	void		    
addLookahead	(pANTLR3_COMMON_TREE_NODE_STREAM ctns, pANTLR3_BASE_TREE node)
{
    *(ctns->lookAhead + ctns->tail) = node;

    ctns->tail = (ctns->tail +1) % ctns->lookAheadLength;

    /* See if we need to resize the buffer at all
     */
    if	(ctns->tail == ctns->head)
    {
	/* Buffer overflow detected, we must allocate a bigger buffer
	 */
	pANTLR3_BASE_TREE   * newBuf;
	ANTLR3_UINT32	      newTail;

	newBuf	= (pANTLR3_BASE_TREE *)ANTLR3_MALLOC( 2 * ctns->lookAheadLength * sizeof(pANTLR3_BASE_TREE));
	
	for (newTail = 0; newTail < ctns->lookAheadLength; newTail++)
	{
	    *(newBuf + newTail) = *(ctns->lookAhead + ((ctns->head + newTail + 1) % ctns->lookAheadLength));
	}

	ANTLR3_FREE(ctns->lookAhead);
	ctns->lookAhead		= newBuf;
	ctns->tail		= newTail;
	ctns->lookAheadLength	= 2 * ctns->lookAheadLength;
    }
}

/** Consume the next node from the input stream
 */
static	void		    
consume	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    /* Make sure that we have at least one node to consume in the lookahead
     * buffer by calling fill, which might call next.
     */
    ctns->fill(ctns, 1);
    ctns->absoluteNodeIndex++;

    /* Track the previous node in case of LT(-1)
     */
    ctns->previousNode		= *(ctns->lookAhead + ctns->head);

    /* Advnace past this node now
     */
    ctns->head	= (ctns->head + 1) % ctns->lookAheadLength;
}

static	ANTLR3_UINT32	    
LA	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 i)
{
    pANTLR3_BASE_TREE	t;

    /* Ask LT for the 'token' at that position
     */
    t = ctns->LT(ctns, i);

    if	(t == NULL)
    {
	return	ANTLR3_TOKEN_INVALID;
    }
    
    /* Token node was so return the type of it
     */
    return  t->getType(t);
}

/** Free up the memory used by a mark state
 */
static	void 
markFree(void * memory)
{
    pANTLR3_TREE_WALK_STATE ts;

    ts = (pANTLR3_TREE_WALK_STATE) memory;

    /* Free the lookahead buffer first
     */
    ANTLR3_FREE(ts->lookAhead);

    /* Now free the structure itself
     */
    ANTLR3_FREE(memory);

}

/** Mark the state of the input stream so that we can come back to it
 * after a syntactic predicate and so on.
 */
static	ANTLR3_UINT64	    
mark	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    pANTLR3_TREE_WALK_STATE	state;

    if	(ctns->markers == NULL)
    {
	ctns->markers = antlr3ListNew(33);
    }

    state = (pANTLR3_TREE_WALK_STATE)ANTLR3_MALLOC(sizeof(ANTLR3_TREE_WALK_STATE));

    if	(state == NULL)
    {
	/* TODO: Create exception for this
	 */
	fprintf(stderr, "Out of memory trying to create a mark state in tree parsing\n");
	return 0;
    }

    /* Now we need to populate the structure
     */
    state->absoluteNodeIndex	= ctns->absoluteNodeIndex;
    state->currentChildIndex	= ctns->currentChildIndex;
    state->currentNode		= ctns->currentNode;
    state->previousNode		= ctns->previousNode;
    state->nodeStackSize	= ctns->nodeStack->size(ctns->nodeStack);

    /* Snap shot the look ahead buffer next
     */
    state->lookAhead		= (pANTLR3_BASE_TREE *)ANTLR3_MALLOC(sizeof(pANTLR3_BASE_TREE) * ctns->lookAheadLength);
    memcpy((void *)state->lookAhead, (const void *)ctns->lookAhead, (sizeof(pANTLR3_BASE_TREE) * ctns->lookAheadLength));
    state->lookAheadLength	= ctns->lookAheadLength;
    state->head			= ctns->head;
    state->tail			= ctns->tail;
    
    /* Add the walk state information, with a custom free routine that knows how to 
     * give back the memory we just used. When we reinstate this, we will first just use
     * the top pointerm so copy it back to the current state, then pop it, which 
     * will cause the memory used to be freed up.
     */
    ctns->markers->add(ctns->markers, (void *)state, markFree);

    /* Return the current mark point
     */
    ctns->lastMarker = ctns->markers->size(ctns->markers);
    return ctns->lastMarker;
}

static	void		    
release	(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 marker)
{
    fprintf(stderr, "Cannot release a tree parse\n");
}

/** Rewind the current state of the tree walk to the state it
 *  was in when mark() was called and it returned marker.  Also,
 *  wipe out the lookahead which will force reloading a few nodes
 *  but it is better than making a copy of the lookahead buffer
 *  upon mark().
 */
static	void		    
rewindMark	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 marker)
{
    pANTLR3_TREE_WALK_STATE	state;
    ANTLR3_UINT64		m;

    if	(ctns->markers == NULL || ctns->markers->size(ctns->markers) < marker)
    {
	return;	    // No such marker - do nothing
    }

    /* Retrieve the marker at the specified mark
     */
    state   = (pANTLR3_TREE_WALK_STATE) ctns->markers->get(ctns->markers, marker);

    /* Now reset the current state
     */
    ctns->absoluteNodeIndex	= state->absoluteNodeIndex;
    ctns->currentChildIndex	= state->currentChildIndex;
    ctns->currentNode		= state->currentNode;
    ctns->previousNode		= state->previousNode;

    /* Drop the current node stack back to the size it was when 
     * we set the mark.
     */
    for	(m = ctns->nodeStack->size(ctns->nodeStack); m > state->nodeStackSize; m--)
    {
	ctns->nodeStack->pop(ctns->nodeStack);
    }

    /* Now we can throw away the current look ahead stack (because we allocated a new one
     * when we mark()ed this, and reinstate the one we saved.
     */
    ANTLR3_FREE(ctns->lookAhead);
    ctns->lookAheadLength   = state->lookAheadLength;
    ctns->head		    = state->head;
    ctns->tail		    = state->tail;
    ctns->lookAhead	    = state->lookAhead;

    /* We now need to remove any markers that were added after this marker and
     * this marker itself. The remove will cuase any space allocated to be returned
     * to the system.
     */
    for	(m = ctns->markers->size(ctns->markers); m >= marker; m--)
    {
	ctns->markers->remove(ctns->markers, m);
    }
}

static	void		    
rewindInput	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    ctns->rewindMark(ctns, ctns->lastMarker);
}

/** consume() ahead until we hit index.  Can't just jump ahead--must
 *  spit out the navigation nodes.
 */
static	void		    
seek	(pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 index)
{
    /* Check for trying to seek in reverse
     */
    if	(index < ctns->index(ctns))
    {
	fprintf(stderr, "Can't seek backwards in a node stream\n");
    }

    /* Seek forwards, so we consume nodes until we arrive at the
     * index.
     */
    while   (ctns->index(ctns) < index)
    {
	ctns->consume(ctns);
    }
}

static	ANTLR3_UINT64		    
index	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return ctns->absoluteNodeIndex + 1;
}

/** Expensive to compute the size of the whole tree while parsing.
 *  This method only returns how much input has been seen so far.  So
 *  after parsing it returns true size.
 */
static	ANTLR3_UINT64		    
size	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return ctns->absoluteNodeIndex + 1;
}

static	ANTLR3_BOOLEAN	    
hasNext	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return (ctns->currentNode != NULL);
}

/** Return the next node found during a depth-first walk of root.
 *  Also, add these nodes and DOWN/UP imaginary nodes into the lokoahead
 *  buffer as a side-effect.  Normally side-effects are bad, but because
 *  we can emit many tokens for every next() call, it's pretty hard to
 *  use a single return value for that.  We must add these tokens to
 *  the lookahead buffer.
 *
 *  This does *not* return the DOWN/UP nodes; those are only returned
 *  by the LT() method.
 *
 *  Ugh.  This mechanism is much more complicated than a recursive
 *  solution, but it's the only way to provide nodes on-demand instead
 *  of walking once completely through and buffering up the nodes. :(
 */
static	pANTLR3_BASE_TREE	    
next	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    /* Already walked the tree?
     */
    if	(ctns->currentNode == NULL)
    {
	ctns->addLookahead(ctns, &(ctns->EOF_NODE));
	return	NULL;
    }

    /* Initial conditions? (First time this has been called)
     */
    if	(ctns->currentChildIndex == -1)
    {
	return ctns->handleRootnode(ctns);
    }

    /* Index is in the child list?
     */
    if	(ctns->currentChildIndex < (ANTLR3_INT64)(ctns->currentNode->getChildCount(ctns->currentNode)) )
    {
	return	ctns->visitChild(ctns, ctns->currentChildIndex);
    }

    /* We must have hit the end of the child list, so we return to the
     * parent node, or it's parent, or it's and so on...
     */
    ctns->walkBackToMostRecentNodeWithUnvisitedChildren(ctns);

    if	(ctns->currentNode != NULL)
    {
	return ctns->visitChild(ctns, ctns->currentChildIndex);
    }

    /* Well, nothing left to do...
     */
    return  NULL;
}

static	pANTLR3_BASE_TREE	    
handleRootnode	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    pANTLR3_BASE_TREE	node;

    /* Start with the current node in the stream
     */
    node    = ctns->currentNode;

    /* We start with the first child of the current node ready
     * to perform a next.
     */
    ctns->currentChildIndex = 0;

    if	(node->isNil(node))
    {
	/* We don;t want to count this root nil node so move on
	 */
	node	= ctns->visitChild(ctns, ctns->currentChildIndex);
    }
    else
    {
	/* Root node is not nil, so add it to lookahead and see if there are
	 * children.
	 */
	ctns->addLookahead(ctns, node);
	
	if  ( ctns->currentNode->getChildCount(ctns) == 0)
	{
	    /* This is a single node with no children
	     */
	    ctns->currentNode = NULL;
	}
    }

    /* Return the node we arrived at one way or another.
     */
    return node;
}

/** Push the current node and descend to the child nodes.
 */
static	pANTLR3_BASE_TREE	    
visitChild	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT64 child)
{
    pANTLR3_BASE_TREE	node;

    node = NULL;

    /* Save the current state of nodes. The specified child index is stored
     * so we can travese from this point after the children are all traversed.
     */
    ctns->currentNode->savedIndex = child;
    ctns->nodeStack->push(ctns->nodeStack, ctns->currentNode, NULL);

    /* Add the DOWN navigation node as we descend
     */
    if	(child == 0 && !ctns->currentNode->isNil(ctns->currentNode))
    {
	ctns->addNavigationNode(ctns, ANTLR3_TOKEN_DOWN);
    }
    
    /* Now visit the child
     */	
    ctns->currentNode		= ctns->currentNode->getChild(ctns->currentNode, child);
    ctns->currentChildIndex	= 0;
    node			= ctns->currentNode;
    ctns->addLookahead(ctns, node);

    ctns->walkBackToMostRecentNodeWithUnvisitedChildren(ctns);

    return node;
}

/** As we flatten the tree, we use UP, DOWN nodes to represent
 *  the tree structure.  When debugging we need unique nodes
 *  so instantiate new ones when uniqueNavigationNodes is true.
 */
static	void		    
addNavigationNode	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_UINT32 ttype)
{
    pANTLR3_BASE_TREE	    node;

    node = NULL;

    if	(ttype == ANTLR3_TOKEN_DOWN)
    {
	if  (ctns->hasUniqueNavigationNodes(ctns))
	{
	    node    = ctns->newDownNode(ctns);
	}
	else
	{
	    node    = &(ctns->DOWN);
	}
    }
    else
    {
	if  (ctns->hasUniqueNavigationNodes(ctns))
	{
	    node    = ctns->newUpNode(ctns);
	}
	else
	{
	    node    = &(ctns->UP);
	}
    }

    /* Now add the node we decided upon.
     */
    ctns->addLookahead(ctns, node);
}

/** Walk upwards looking for a node with more children to walk
 *  using a function with a name almost as long as this sentence
 */
static	void		    
walkBackToMostRecentNodeWithUnvisitedChildren	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
   // while   (	    (ctns->currentNode != NULL)
//		&&  (ctns->currentChildIndex(ctns) >= ctns->currentNode->getChildCount(ctns->currentNode))
//	    )
    {
	ctns->currentNode	= (pANTLR3_BASE_TREE) (ctns->nodeStack->top);
	ctns->nodeStack->pop(ctns->nodeStack);		// Remove top element now

	/* Move to the next child after the one the we just traversed. The index of the one we just traversed
	 * was stored in the current node we saved upon our stack.
	 */
	ctns->currentChildIndex	= ctns->currentNode->savedIndex + 1;

	if  (ctns->currentChildIndex >= (ANTLR3_INT64)(ctns->currentNode->getChildCount(ctns->currentNode)) )
	{
	    if	( ! ctns->currentNode->isNil(ctns->currentNode) )
	    {
		ctns->addNavigationNode(ctns, ANTLR3_TOKEN_UP);
	    }

	    /* Are we there yet?
	     */
	    if	(ctns->currentNode == ctns->root)
	    {
		/* We arrived all the way back at the root node, so our depth first walk
		 * must be finished.
		 */
		ctns->currentNode   = NULL;
	    }
	}
    }
}

static	pANTLR3_BASE_TREE_ADAPTOR			    
getTreeAdaptor	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return  ctns->adaptor;
}

static	ANTLR3_BOOLEAN	    
hasUniqueNavigationNodes	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return  ctns->uniqueNavigationNodes;
}

static	void		    
setUniqueNavigationNodes	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns, ANTLR3_BOOLEAN uniqueNavigationNodes)
{
    ctns->uniqueNavigationNodes = uniqueNavigationNodes;
}

/** Using the Iterator interface, return a list of all the token types
 *  as text.  Used for testing.
 */
static	pANTLR3_STRING	    
toNodesOnlyString	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    pANTLR3_STRING  buf;

    buf = ctns->stringFactory->newRaw(ctns->stringFactory);

    while   (ctns->hasNext(ctns))
    {
	pANTLR3_BASE_TREE   t;

	t = ctns->next(ctns);

	buf->append (buf, (pANTLR3_UINT8)" ");
	buf->addi   (buf, t->getType(t));
    }

    return  buf;
}

/** Print out the entire tree including DOWN/UP nodes.  Uses
 *  a recursive walk.  Mostly useful for testing as it yields
 *  the token types not text.
 */
static	pANTLR3_STRING	    
toString	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return  ctns->toStringSS(ctns, ctns->root, NULL);
}

static	pANTLR3_STRING	    
toStringSS	    (pANTLR3_COMMON_TREE_NODE_STREAM ctns, pANTLR3_BASE_TREE start, pANTLR3_BASE_TREE stop)
{
    pANTLR3_STRING  buf;

    buf = ctns->stringFactory->newRaw(ctns->stringFactory);

    ctns->toStringWork(ctns, start, stop, buf);

    return  buf;
}

static	void	    
toStringWork	(pANTLR3_COMMON_TREE_NODE_STREAM ctns, pANTLR3_BASE_TREE p, pANTLR3_BASE_TREE stop, pANTLR3_STRING buf)
{

    ANTLR3_UINT64   n;
    ANTLR3_UINT64   c;

    if	(!p->isNil(p) )
    {
	pANTLR3_STRING	text;

	text	= p->toString(p);

	if  (text == NULL)
	{
	    text = ctns->stringFactory->newRaw(ctns->stringFactory);

	    text->append    (text, " ");
	    text->addi	    (text, p->getType(p));
	}

	buf->append(buf, text->text);
    }

    if	(p == stop)
    {
	return;		/* Finished */
    }

    n = p->getChildCount(p);

    if	(n > 0 && ! p->isNil(p) )
    {
	buf->append (buf, " ");
	buf->addi   (buf, ANTLR3_TOKEN_DOWN);
    }

    for	(c = 0; c<n ; c++)
    {
	pANTLR3_BASE_TREE   child;

	child = p->getChild(p, c);
	ctns->toStringWork(ctns, child, stop, buf);
    }

    if	(n > 0 && ! p->isNil(p) )
    {
	buf->append (buf, " ");
	buf->addi   (buf, ANTLR3_TOKEN_UP);
    }
}

static	ANTLR3_UINT32	    
getLookaheadSize	(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
    return	ctns->tail < ctns->head 
	    ?	(ctns->lookAheadLength - ctns->head + ctns->tail)
	    :	(ctns->tail - ctns->head);
}

static	pANTLR3_BASE_TREE	    
newDownNode		(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
}

static	pANTLR3_BASE_TREE	    
newUpNode		(pANTLR3_COMMON_TREE_NODE_STREAM ctns)
{
}